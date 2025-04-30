package decoders.java.util;

import org.jacodb.api.jvm.*;
import org.usvm.api.SymbolicMap;
import org.usvm.api.decoder.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.usvm.api.decoder.DecoderUtils.findStorageField;
import static org.usvm.api.decoder.DecoderUtils.getAllMethods;

@DecoderFor(HashMap.class)
public class HashMap_Decoder implements ObjectDecoder {
    private volatile JcMethod[] cachedMethods = null;
    private volatile JcMethod cached_HashMap_ctor = null;
    private volatile JcMethod cached_HashMap_put = null;
    private volatile JcField cached_HashMap_storage = null;
    private volatile JcField cached_Map_map = null;
    private volatile JcField cached_HashMapContainer_map = null;
    private volatile JcMethod cached_MapEntry_getValue = null;

    @Override
    public <T> T createInstance(final JcClassOrInterface approximation,
                                final ObjectData<T> approximationData,
                                final DecoderApi<T> decoder) {
        JcMethod m_ctor = cached_HashMap_ctor;
        // TODO: add synchronization if needed
        if (m_ctor == null) {
            final List<JcMethod> methodList = approximation.getDeclaredMethods();
            final int methodCount = methodList.size();
            JcMethod[] methods = new JcMethod[methodCount];
            cachedMethods = methods = methodList.toArray(methods);

            for (int i = 0; i != methodCount; i++) {
                JcMethod m = methods[i];

                if (m.isConstructor()) {
                    List<JcParameter> params = m.getParameters();

                    // example: looking for java.util.HashMap.HashMap()
                    if (!params.isEmpty()) continue;

                    // update global "cache" and stop the search
                    cached_HashMap_ctor = m_ctor = m;
                    break;
                }
            }
        }

        // prepare parameters "in-place" and construct a new call
        final ArrayList<T> args = new ArrayList<>();
        return decoder.invokeMethod(m_ctor, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void initializeInstance(final JcClassOrInterface approximation,
                                       final ObjectData<T> approximationData,
                                       final T outputInstance,
                                       final DecoderApi<T> decoder) {
        JcField f_hs_storage = cached_HashMap_storage;
        // TODO: add synchronization if needed
        if (f_hs_storage == null) {
            cached_HashMap_storage = f_hs_storage = findStorageField(approximation);
        }

        // skip empty or erroneous objects
        final ObjectData<T> storageData = approximationData.getObjectField(f_hs_storage);
        if (storageData == null)
            return;

        // get primary method
        JcMethod m_put = cached_HashMap_put;
        // TODO: add synchronization if needed
        if (m_put == null) {
            // TODO: add cache
            List<JcMethod> methodsList = getAllMethods(approximation);
            final int methodCount = methodsList.size();
            JcMethod[] methods = new JcMethod[methodCount];
            methodsList.toArray(methods);
            for (int i = 0, c = methods.length; i != c; i++) {
                JcMethod m = methods[i];

                if (!"put".equals(m.getName())) continue;
                List<JcParameter> params = m.getParameters();
                if (params.size() != 2) continue;

                m_put = m;
                break;
            }
            cached_HashMap_put = m_put;
        }

        // prepare field references (inlined)
        JcField f_m_map = cached_Map_map;
        // TODO: add synchronization if needed
        if (f_m_map == null) {
            JcClasspath cp = approximation.getClasspath();
            {
                List<JcField> fields = cp.findClassOrNull("runtime.LibSLRuntime$Map").getDeclaredFields();
                for (int i = 0, c = fields.size(); i != c; i++) {
                    JcField field = fields.get(i);

                    if ("map".equals(field.getName())) {
                        cached_Map_map = f_m_map = field;
                        break;
                    }
                }
            }
            {
                List<JcField> fields = cp.findClassOrNull("runtime.LibSLRuntime$HashMapContainer").getDeclaredFields();
                for (int i = 0, c = fields.size(); i != c; i++) {
                    JcField field = fields.get(i);

                    if ("map".equals(field.getName())) {
                        cached_HashMapContainer_map = field;
                        break;
                    }
                }
            }
        }

        // get and parse the underlying symbolic map
        final ObjectData<T> rtMapContainerData = storageData.getObjectField(f_m_map);
        if (rtMapContainerData == null)
            return; // ignore invalid container

        final SymbolicMap<T, T> map = rtMapContainerData.decodeSymbolicMapField(cached_HashMapContainer_map);
        if (map == null)
            return; // ignore invalid container

        int length = map.size();
        if (length == Integer.MAX_VALUE)
            return; // ignore invalid container

        while (length > 0) {
            T key = map.anyKey();
            T value = map.get(key);
            if (value instanceof InternalMapEntry) {
                value = ((InternalMapEntry<T, T>) value).getValue();
            }

            List<T> args = new ArrayList<>();
            args.add(outputInstance);
            args.add(key);
            args.add(value);
            decoder.invokeMethod(m_put, args);

            map.remove(key);
            length--;
        }
    }
}
