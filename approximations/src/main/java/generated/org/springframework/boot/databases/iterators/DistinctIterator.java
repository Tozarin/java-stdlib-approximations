package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.ITable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class DistinctIterator<T> implements Iterator<T> {

    Iterator<T> tblIter;
    T curr;

    Set<T> cache;

    public DistinctIterator(ITable<T> table) {
        this(table, false);
    }

    public DistinctIterator(ITable<T> table, boolean reversed) {
        this.tblIter = reversed ? table.backIterator() : table.iterator();
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
