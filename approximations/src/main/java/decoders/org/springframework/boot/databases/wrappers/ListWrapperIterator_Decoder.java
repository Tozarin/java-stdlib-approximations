package decoders.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.iterators.wrappers.ListWrapperIterator;
import org.jacodb.api.jvm.JcClassOrInterface;
import org.jacodb.api.jvm.JcClasspath;
import org.jacodb.api.jvm.JcField;
import org.jacodb.api.jvm.JcMethod;
import org.usvm.api.decoder.DecoderApi;
import org.usvm.api.decoder.DecoderFor;
import org.usvm.api.decoder.ObjectData;
import org.usvm.api.decoder.ObjectDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@DecoderFor(ListWrapperIterator.class)
public final class ListWrapperIterator_Decoder implements ObjectDecoder {

    private volatile JcMethod cached_ArrayList_ctor = null;
    private volatile JcMethod cached_ArrayList_iterator = null;

    private volatile JcMethod cached_Iterator_next = null;

    private volatile JcField cached_ListWrapperIterator_ix = null;
    private volatile JcField cached_ListWrapperIterator_list = null;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(
            JcClassOrInterface approximation,
            ObjectData<T> approximationData,
            DecoderApi<T> decoder) {

        final List<JcField> fields = approximation.getDeclaredFields();
        if (cached_ListWrapperIterator_ix == null || cached_ListWrapperIterator_list == null) {
            for (JcField f : fields) {
                if (cached_ListWrapperIterator_ix == null && "ix".equals(f.getName()))
                    cached_ListWrapperIterator_ix = f;
                if (cached_ListWrapperIterator_list == null && "list".equals(f.getName()))
                    cached_ListWrapperIterator_list = f;
                if (cached_ListWrapperIterator_ix != null && cached_ListWrapperIterator_list != null) break;
            }
        }

        if (cached_ArrayList_iterator == null || cached_ArrayList_ctor == null) {
            JcClassOrInterface listClass = approximation.getClasspath().findClassOrNull("java.util.ArrayList");
            List<JcMethod> methods = listClass.getDeclaredMethods();
            for (JcMethod method : methods) {
                if (cached_ArrayList_iterator == null && "iterator".equals(method.getName()) && method.getParameters().isEmpty()) {
                    cached_ArrayList_iterator = method;
                }
                if (cached_ArrayList_ctor == null && method.isConstructor() && method.getParameters().isEmpty()) {
                    cached_ArrayList_ctor = method;
                }
                if (cached_ArrayList_iterator != null && cached_ArrayList_ctor != null) break;
            }
        }

        if (cached_Iterator_next == null) {
            JcClassOrInterface iteratorInterface = approximation.getClasspath().findClassOrNull("java.util.Iterator");
            List<JcMethod> methods = iteratorInterface.getDeclaredMethods();
            for (JcMethod method : methods) {
                if ("next".equals(method.getName()) && method.getParameters().isEmpty()) {
                    cached_Iterator_next = method;
                    break;
                }
            }
        }

        int ix = approximationData.getIntField(cached_ListWrapperIterator_ix);

        T list = approximationData.getObjectField(cached_ListWrapperIterator_list) != null
                ? approximationData.decodeField(cached_ListWrapperIterator_list)
                : decoder.invokeMethod(cached_ArrayList_ctor, (List<T>) Collections.EMPTY_LIST);

        final ArrayList<T> iter_args = new ArrayList<>();
        iter_args.add(list);
        T listIter = decoder.invokeMethod(cached_ArrayList_iterator, iter_args);

        final ArrayList<T> next_args = new ArrayList<>();
        next_args.add(listIter);
        for (int i = 0; i < ix; i++) decoder.invokeMethod(cached_Iterator_next, next_args);

        return listIter;
    }

    @Override
    public <T> void initializeInstance(
            JcClassOrInterface approximation,
            ObjectData<T> approximationData,
            T t,
            DecoderApi<T> decoder) {
        // nothing
    }
}
