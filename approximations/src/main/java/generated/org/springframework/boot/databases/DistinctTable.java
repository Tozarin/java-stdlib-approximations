package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.DistinctIterator;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DistinctTable<T> implements ITable<T> {

    public ITable<T> table;

    public Set<T> cache;
    public int cacheSize;

    public DistinctTable(ITable<T> table) {
        this.table = table;
        this.cache = new HashSet<>();
        this.cacheSize = -1;
    }

    public DistinctTable(ITable<T> table, Set<T> cache, int cacheSize) {
        this.table = table;
        this.cache = cache;
        this.cacheSize = cacheSize;
    }

    @Override
    public int size() {

        if (cacheSize != -1) return cacheSize;

        Iterator<T> iter = new DistinctIterator<>(table);
        int count = 0;
        while (iter.hasNext()) {
            if (cache.add(iter.next())) count++;
        }

        cacheSize = count;
        return cacheSize;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        if (cacheSize != -1) return cache.iterator();
        return new DistinctIterator<>(table);
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public T first() {
        Iterator<T> iter = iterator();
        if (!iter.hasNext()) return null;
        return iter.next();
    }
}
