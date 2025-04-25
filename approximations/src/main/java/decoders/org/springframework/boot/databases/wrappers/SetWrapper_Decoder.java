package decoders.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.wrappers.SetWrapper;
import org.jacodb.api.jvm.*;
import org.usvm.api.decoder.DecoderApi;
import org.usvm.api.decoder.DecoderFor;
import org.usvm.api.decoder.ObjectData;
import org.usvm.api.decoder.ObjectDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.usvm.api.decoder.DecoderUtils.getAllFields;

@SuppressWarnings("ForLoopReplaceableByForEach")
@DecoderFor(SetWrapper.class)
public final class SetWrapper_Decoder implements ObjectDecoder {

    private volatile JcMethod cached_HashSet_ctor = null;
    private volatile JcMethod cached_HashSet_addAll = null;
    private volatile JcMethod cached_HashSet_removeAll = null;
    private volatile JcField cached_SetWrapper_cache = null;
    private volatile JcField cached_SetWrapper_removedCache = null;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createInstance(final JcClassOrInterface approximation,
                                final ObjectData<T> approximationData,
                                final DecoderApi<T> decoder) {

        JcMethod m_ctor = cached_HashSet_ctor;
        JcMethod m_add = cached_HashSet_addAll;
        JcMethod m_remove = cached_HashSet_removeAll;
        if (m_ctor == null || m_add == null || m_remove == null) {
            JcClasspath cp = approximation.getClasspath();
            JcClassOrInterface arrayListClass = cp.findClassOrNull("java.util.HashSet");
            final List<JcMethod> methods = arrayListClass.getDeclaredMethods();
            for (int i = 0, c = methods.size(); i < c; i++) {

                JcMethod m = methods.get(i);
                if (m.isConstructor()) {
                    if (m_ctor == null) {
                        if (!m.getParameters().isEmpty()) continue;

                        cached_HashSet_ctor = m_ctor = m;
                    }
                } else if ("addAll".equals(m.getName())) {
                    if (m_add == null) {

                        List<JcParameter> params = m.getParameters();
                        if (params.size() != 1) continue;
                        if (!"java.util.Collection".equals(params.get(0).getType().getTypeName())) continue;

                        cached_HashSet_addAll = m_add = m;
                    }
                } else {
                    if (m_remove == null) {
                        if (!"removeAll".equals(m.getName())) continue;

                        List<JcParameter> params = m.getParameters();
                        if (params.size() != 1) continue;
                        if (!"java.util.Collection".equals(params.get(0).getType().getTypeName())) continue;

                        cached_HashSet_removeAll = m_add = m;
                    }
                }

                if (m_ctor != null && m_add != null && m_remove != null)
                    break;
            }
        }

        return decoder.invokeMethod(m_ctor, (List<T>) Collections.EMPTY_LIST);
    }

    @Override
    public <T> void initializeInstance(final JcClassOrInterface approximation,
                                       final ObjectData<T> approximationData,
                                       final T outputInstance,
                                       final DecoderApi<T> decoder) {

        JcField f_cache = cached_SetWrapper_cache;
        JcField f_removed = cached_SetWrapper_removedCache;
        if (f_cache == null || f_removed == null) {
            for (JcField field : getAllFields(approximation)) {
                if ("cache".equals(field.getName())) {
                    cached_SetWrapper_cache = f_cache = field;
                    break;
                } else if ("removedCache".equals(field.getName())) {
                    cached_SetWrapper_removedCache = f_removed = field;
                    break;
                }
            }
        }

        if (approximationData.getObjectField(f_cache) == null)
            return;

        final T cache = approximationData.decodeField(f_cache);
        if (cache == null)
            return;

        ArrayList<T> addAllArgs = new ArrayList<>();
        addAllArgs.add(outputInstance);
        addAllArgs.add(cache);
        decoder.invokeMethod(cached_HashSet_addAll, addAllArgs);

        if (approximationData.getObjectField(f_removed) == null)
            return;

        final T removed = approximationData.decodeField(f_removed);
        if (removed == null)
            return;

        ArrayList<T> removeAllArgs = new ArrayList<>();
        removeAllArgs.add(outputInstance);
        removeAllArgs.add(cache);
        decoder.invokeMethod(cached_HashSet_removeAll, removeAllArgs);
    }
}
