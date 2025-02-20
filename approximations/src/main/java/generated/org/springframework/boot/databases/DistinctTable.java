package generated.org.springframework.boot.databases;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
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

        Iterator<T> iter = new DistinctIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (cache.add(iter.next())) count++;
        }

        cacheSize = count;
        return cacheSize;
    }

    class DistinctIterator implements Iterator<T> {

        Iterator<T> tblIter;
        T curr;

        Set<T> cache;

        public DistinctIterator(Iterator<T> tblIter) {
            this.tblIter = tblIter;
            this.curr = null;
            this.cache = new HashSet<>();
        }

        public DistinctIterator() {
            this.tblIter = table.clone().iterator();
            this.curr = null;
            this.cache = new HashSet<>();
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            while (tblIter.hasNext()) {
                T candidate = tblIter.next();
                if (cache.add(candidate)) {
                    curr = candidate;
                    return true;
                }
            }

            return false;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();

            T tmp = curr;
            curr = null;

            return tmp;
        }
    }

    @Override
    public Iterator<T> iterator() {
        if (cacheSize != -1) return cache.iterator();
        return new DistinctIterator();
    }

    @Override
    public Iterator<T> backIterator() {
        return new DistinctIterator(table.clone().backIterator());
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public ITable<T> clone() {
        return new DistinctTable<>(
                table.clone(),
                cache,
                cacheSize
        );
    }
}
