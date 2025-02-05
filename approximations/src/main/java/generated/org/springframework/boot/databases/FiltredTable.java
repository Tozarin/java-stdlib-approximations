package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class FiltredTable<T> implements ITable<T> {

    public ITable<T> table;
    public Predicate<T> filter;

    public List<T> cache;
    public int cacheSize;

    public FiltredTable(ITable<T> table, Predicate<T> filter) {
        this.table = table;
        this.filter = filter;

        this.cache = new ArrayList<>();
        this.cacheSize = -1;
    }

    public FiltredTable(
            ITable<T> table,
            Predicate<T> filter,
            List<T> cache,
            int cacheSize
    ) {
        this.table = table;
        this.filter = filter;
        this.cache = cache;
        this.cacheSize = cacheSize;
    }

    public int size() {

        if (cacheSize != -1) return cacheSize;

        Iterator<T> iter = iterator();
        int count = 0;
        while (iter.hasNext()) {
            T candidate = iter.next();
            if (filter.test(candidate)) {
                count++;
                cache.add(candidate);
            }
        }

        cacheSize = count;
        return cacheSize;
    }

    class FiltredIterator implements Iterator<T> {

        Iterator<T> tblIter;
        T curr;

        public FiltredIterator() {
            this.tblIter = table.clone().iterator();
            this.curr = null;
        }

        public FiltredIterator(Iterator<T> tblIter) {
            this.tblIter = tblIter;
            this.curr = null;
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            while (tblIter.hasNext()) {
                T candidate = tblIter.next();
                if (filter.test(candidate)) {
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

    public Iterator<T> iterator() {
        if (cacheSize != -1) return cache.iterator();
        return new FiltredIterator();
    }

    public Iterator<T> backIterator() {
        return new FiltredIterator(table.clone().backIterator());
    }

    public Class<T> type() {
        return table.type();
    }

    public ITable<T> clone() {
        return new FiltredTable<>(
                table.clone(),
                filter,
                cache,
                cacheSize
        );
    }

    public T firstEnsure() {
        Iterator<T> iter = iterator();
        Engine.assume(iter.hasNext());
        return iter.next();
    }
}
