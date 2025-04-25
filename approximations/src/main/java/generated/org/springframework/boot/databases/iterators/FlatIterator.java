package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.FlatTable;
import generated.org.springframework.boot.databases.ITable;
import org.usvm.api.Engine;

import java.util.Iterator;

public class FlatIterator<T> implements Iterator<T> {

    Iterator<ITable<T>> iters;
    Iterator<T> currIter;

    public FlatIterator(FlatTable<T> flat) {
        this.iters = flat.tables.iterator();
        this.currIter = null;
    }

    private void nextCurr() {
        ITable<T> next = iters.next();
        currIter = next.iterator();
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
