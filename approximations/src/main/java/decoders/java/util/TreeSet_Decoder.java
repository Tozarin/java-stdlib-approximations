package decoders.java.util;

import org.jacodb.api.jvm.*;
import org.usvm.api.SymbolicMap;
import org.usvm.api.decoder.DecoderApi;
import org.usvm.api.decoder.DecoderFor;
import org.usvm.api.decoder.ObjectData;
import org.usvm.api.decoder.ObjectDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.usvm.api.decoder.DecoderUtils.findStorageField;

@DecoderFor(TreeSet.class)
public final class TreeSet_Decoder implements ObjectDecoder {
    private volatile JcMethod[] cachedMethods = null;
    private volatile JcMethod cached_TreeSet_ctor = null;
    private volatile JcMethod cached_TreeSet_add = null;
    private volatile JcField cached_TreeSet_storage = null;
    private volatile JcField cached_Map_map = null;
    private volatile JcField cached_HashMapContainer_map = null;

    @Override
    public <T> T createInstance(final JcClassOrInterface approximation,
                                final ObjectData<T> approximationData,
                                final DecoderApi<T> decoder) {
        JcMethod ctor = cached_TreeSet_ctor;
        // TODO: add synchronization if needed
        if (ctor == null) {
            final List<JcMethod> methodList = approximation.getDeclaredMethods();
            final int methodCount = methodList.size();
            JcMethod[] methods = new JcMethod[methodCount];
            cachedMethods = methods = methodList.toArray(methods);

            for (int i = 0; i != methodCount; i++) {
                JcMethod m = methods[i];

                if (m.isConstructor()) {
                    List<JcParameter> params = m.getParameters();
                    if (!params.isEmpty()) continue;
                    cached_TreeSet_ctor = ctor = m;
                    break;
                }
            }
        }

        final List<T> args = new ArrayList<>();
        return decoder.invokeMethod(ctor, args);
    }

    @Override
    public <T> void initializeInstance(final JcClassOrInterface approximation,
                                       final ObjectData<T> approximationData,
                                       final T outputInstance,
                                       final DecoderApi<T> decoder) {
        JcField f_ts_storage = cached_TreeSet_storage;
        // TODO: add synchronization if needed
        if (f_ts_storage == null) {
            cached_TreeSet_storage = f_ts_storage = findStorageField(approximation);
        }

        final ObjectData<T> storageData = approximationData.getObjectField(f_ts_storage);
        if (storageData == null)
            return;

        JcMethod m_add = cached_TreeSet_add;
        // TODO: add synchronization if needed
        if (m_add == null) {
            final JcMethod[] methods = cachedMethods;
            for (int i = 0, c = methods.length; i != c; i++) {
                JcMethod m = methods[i];

                if (!"add".equals(m.getName())) continue;
                List<JcParameter> params = m.getParameters();
                if (params.size() != 1) continue;
                if (!"java.lang.Object".equals(params.get(0).getType().getTypeName())) continue;

                m_add = m;
                break;
            }
            cached_TreeSet_add = m_add;
        }

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

        final ObjectData<T> rtMapContainerData = storageData.getObjectField(f_m_map);
        if (rtMapContainerData == null)
            return;

        final SymbolicMap<T, T> map = rtMapContainerData.decodeSymbolicMapField(cached_HashMapContainer_map);
        if (map == null)
            return;

        int length = map.size();
        if (length == Integer.MAX_VALUE)
            return;

        while (length > 0) {
            T key = map.anyKey();

            List<T> args = new ArrayList<>();
            args.add(outputInstance);
            args.add(key);
            decoder.invokeMethod(m_add, args);

            map.remove(key);
            length--;
        }
    }
}
