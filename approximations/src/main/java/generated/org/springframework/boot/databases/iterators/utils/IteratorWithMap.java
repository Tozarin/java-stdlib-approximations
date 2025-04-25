package generated.org.springframework.boot.databases.iterators.utils;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorWithMap<T, R> implements Iterator<R> {

    Iterator<T> iter;
    Function<T, R> mapper;

    public IteratorWithMap(Iterator<T> iter, Function<T, R> mapper) {
        this.iter = iter;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(iter.next());
    }
}
