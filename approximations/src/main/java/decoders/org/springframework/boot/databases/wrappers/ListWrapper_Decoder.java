package decoders.org.springframework.boot.databases.wrappers;

import org.jacodb.api.jvm.*;
import org.usvm.api.SymbolicList;
import org.usvm.api.decoder.DecoderApi;
import org.usvm.api.decoder.DecoderFor;
import org.usvm.api.decoder.ObjectData;
import org.usvm.api.decoder.ObjectDecoder;
import stub.spring.ListWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.usvm.api.decoder.DecoderUtils.getAllFields;

@SuppressWarnings("ForLoopReplaceableByForEach")
@DecoderFor(ListWrapper.class)
public final class ListWrapper_Decoder implements ObjectDecoder {

    private volatile JcMethod cached_ArrayList_ctor = null;
    private volatile JcMethod cached_ArrayList_add = null;
    private volatile JcField cached_ListWrapper_cache = null;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createInstance(final JcClassOrInterface approximation,
                                final ObjectData<T> approximationData,
                                final DecoderApi<T> decoder) {

        JcMethod m_ctor = cached_ArrayList_ctor;
        JcMethod m_add = cached_ArrayList_add;
        if (m_ctor == null || m_add == null) {
            JcClasspath cp = approximation.getClasspath();
            JcClassOrInterface arrayListClass = cp.findClassOrNull("java.util.ArrayList");
            final List<JcMethod> methods = arrayListClass.getDeclaredMethods();
            for (int i = 0, c = methods.size(); i < c; i++) {
                JcMethod m = methods.get(i);

                if (m.isConstructor()) {
                    if (m_ctor == null) {
                        if (!m.getParameters().isEmpty()) continue;

                        cached_ArrayList_ctor = m_ctor = m;
                    }
                } else {
                    if (m_add == null) {
                        if (!"add".equals(m.getName())) continue;

                        List<JcParameter> params = m.getParameters();
                        if (params.size() != 1) continue;
                        if (!"java.lang.Object".equals(params.get(0).getType().getTypeName())) continue;

                        cached_ArrayList_add = m_add = m;
                    }
                }

                if (m_ctor != null && m_add != null)
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

        JcField f_cache = cached_ListWrapper_cache;
        if (f_cache == null) {
            for (JcField field : getAllFields(approximation)) {
                if ("cache".equals(field.getName())) {
                    cached_ListWrapper_cache = f_cache = field;
                    break;
                }
            }
        }

        if (approximationData.getObjectField(f_cache) == null)
            return;

        final SymbolicList<T> cache = approximationData.decodeSymbolicListField(f_cache);
        if (cache == null)
            return;

        for (int i = 0, c = cache.size(); i < c; i++) {
            T t = cache.get(i);
            if (t == null) continue;

            ArrayList<T> args = new ArrayList<>();
            args.add(outputInstance);
            args.add(t);
            decoder.invokeMethod(cached_ArrayList_add, args);
        }
    }
}
