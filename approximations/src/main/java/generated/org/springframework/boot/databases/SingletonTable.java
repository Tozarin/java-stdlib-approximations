package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.SingletonIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SingletonIterator<>(data);
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public T first() {
        return data;
    }
}
