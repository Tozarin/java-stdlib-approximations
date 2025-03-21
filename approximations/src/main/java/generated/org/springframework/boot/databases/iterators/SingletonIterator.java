package generated.org.springframework.boot.databases.iterators;

import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingletonIterator<T> implements Iterator<T> {

    T data;
    boolean returned;

    public SingletonIterator(T data) {
        this.data = data;
        this.returned = false;
    }

    @Override
    public boolean hasNext() {
        return !returned;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        returned = true;
        return data;
    }
}
