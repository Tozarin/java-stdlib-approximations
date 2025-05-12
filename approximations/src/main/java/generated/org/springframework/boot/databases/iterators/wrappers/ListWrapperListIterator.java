package generated.org.springframework.boot.databases.iterators.wrappers;

import generated.org.springframework.boot.databases.wrappers.IListWrapper;

import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListWrapperListIterator<T> implements ListIterator<T> {

    IListWrapper<T> list;

    public int ix;
    public int lastReturnedIx;

    public int expectedModCount;

    public ListWrapperListIterator(IListWrapper<T> list, int ix) {
        this.list = list;
        this.ix = ix;
        this.lastReturnedIx = -1;
        this.expectedModCount = list.getModCount();
    }

    public void concModificationCheck() {
        if (expectedModCount != list.getModCount()) throw new ConcurrentModificationException();
    }

    @Override
    public boolean hasNext() {
        return ix < list.getSizeOfCache();
    }

    @Override
    public T next() {

        if (!hasNext()) throw new NoSuchElementException();
        concModificationCheck();

        lastReturnedIx = ix;

        if (ix < list.getWrpStartIx() || list.getWrpEndIx() <= ix) return list.getFromCache(ix++);

        assert (ix == list.getWrpStartIx());
        list.cacheNext();

        return list.getFromCache(ix++);
    }

    @Override
    public boolean hasPrevious() {
        return 0 < ix;
    }

    @Override
    public T previous() {

        if (!hasPrevious()) throw new NoSuchElementException();
        concModificationCheck();

        lastReturnedIx = ix - 1;

        if (ix <= list.getWrpStartIx() || list.getWrpEndIx() < ix) return list.getFromCache(--ix);

        list.cacheUntilIx(--ix);

        return list.getFromCache(ix);
    }

    @Override
    public int nextIndex() {
        return ix;
    }

    @Override
    public int previousIndex() {
        return ix - 1;
    }

    @Override
    public void remove() {

        if (lastReturnedIx == -1) throw new IllegalStateException();
        concModificationCheck();

        list.remove(lastReturnedIx);
        expectedModCount++;
        ix = lastReturnedIx;

        lastReturnedIx = -1;
    }

    @Override
    public void set(T t) {

        if (lastReturnedIx == -1) throw new IllegalStateException();
        concModificationCheck();

        list.set(lastReturnedIx, t);
        expectedModCount++;

        lastReturnedIx = -1;
    }

    @Override
    public void add(T t) {

        if (lastReturnedIx == -1) throw new IllegalStateException();
        concModificationCheck();

        list.add(lastReturnedIx + 1, t);
        expectedModCount++;
        ix++;

        lastReturnedIx = -1;
    }
}
