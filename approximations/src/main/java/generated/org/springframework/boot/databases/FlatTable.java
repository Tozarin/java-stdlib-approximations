package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.FlatIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class FlatTable<T> implements ITable<T> {

    public ITable<ITable<T>> tables;
    public int size;

    public Class<T> type;

    public FlatTable(ITable<ITable<T>> tables, Class<T> type) {
        this.tables = tables;
        this.size = -1;
        this.type = type;
    }

    public FlatTable(ITable<ITable<T>> tables, int size, Class<T> type) {
        this.tables = tables;
        this.size = size;
        this.type = type;
    }

    @Override
    public int size() {
        if (size != -1) return size;

        int count = 0;
        Iterator<ITable<T>> iter = tables.iterator();
        while (iter.hasNext()) {
            ITable<T> tbl = iter.next();
            count += tbl.size();
        }
        size = count;
        return count;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new FlatIterator<>(this);
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public T first() {
        Iterator<T> iter = iterator();
        if (iter.hasNext()) return iter.next();
        return null;
    }
}
