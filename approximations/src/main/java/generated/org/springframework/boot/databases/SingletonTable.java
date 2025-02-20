package generated.org.springframework.boot.databases;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingletonTable<T> implements ITable<T> {

    public T data;
    Class<T> type;

    public SingletonTable(T data, Class<T> type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public int size() {
        return 1;
    }

    class SingletonIterator implements Iterator<T> {

        boolean returned;

        public SingletonIterator() {
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

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SingletonIterator();
    }

    @Override
    public Iterator<T> backIterator() {
        return new SingletonIterator();
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public ITable<T> clone() {
        return new SingletonTable<>(data, type);
    }
}
