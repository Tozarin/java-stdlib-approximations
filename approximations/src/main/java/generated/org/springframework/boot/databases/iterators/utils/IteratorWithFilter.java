package generated.org.springframework.boot.databases.iterators.utils;

import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorWithFilter<T> implements Iterator<T> {

    public Iterator<T> iter;
    public Function<T, Boolean> filter;
    public T curr;

    public IteratorWithFilter(Iterator<T> iter, Function<T, Boolean> filter) {
        this.iter = iter;
        this.filter = filter;
        this.curr = null;
    }

    @Override
    public boolean hasNext() {

        if (curr != null) return true;

        while (iter.hasNext()) {
            T candidate = iter.next();
            if (filter.apply(candidate)) {
                curr = candidate;
                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        Engine.assume(hasNext());

        T tmp = curr;
        curr = null;

        return tmp;
    }
}
