package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.FlatTable;
import generated.org.springframework.boot.databases.ITable;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FlatIterator<T> implements Iterator<T> {

    Iterator<ITable<T>> iters;
    Iterator<T> currIter;

    boolean reversed;

    public FlatIterator(FlatTable<T> flat) {
        this(flat, false);
    }

    public FlatIterator(FlatTable<T> flat, boolean reversed) {
        this.iters = reversed ? flat.tables.backIterator() : flat.tables.iterator();
        this.currIter = null;
        this.reversed = reversed;
    }

    private void nextCurr() {
        ITable<T> next = iters.next();
        currIter = reversed ? next.backIterator() : next.iterator();
    }

    @Override
    public boolean hasNext() {
        if (currIter == null) {
            if (iters.hasNext()) nextCurr();
            else return false;
        }

        if (currIter.hasNext()) return true;

        if (iters.hasNext()) {
            nextCurr();
            return hasNext();
        }

        return false;
    }

    @Override
    public T next() {
        Engine.assume(hasNext());
        return currIter.next();
    }
}
