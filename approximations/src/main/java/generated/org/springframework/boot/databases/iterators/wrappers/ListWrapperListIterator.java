package generated.org.springframework.boot.databases.iterators.wrappers;


import generated.org.springframework.boot.databases.wrappers.ListWrapper;

import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListWrapperListIterator<T> implements ListIterator<T> {

    ListWrapper<T> list;

    public int ix;
    public int lastReturnedIx;

    public int expectedModCount;

    public ListWrapperListIterator(ListWrapper<T> list, int ix) {
        this.list = list;
        this.ix = ix;
        this.lastReturnedIx = -1;
        this.expectedModCount = list.modCount;
    }

    public void concModificationCheck() {
        if (expectedModCount != list.modCount) throw new ConcurrentModificationException();
    }

    @Override
    public boolean hasNext() {
        return ix < list.sizeOfCache;
    }

    @Override
    public T next() {

        if (!hasNext()) throw new NoSuchElementException();
        concModificationCheck();

        lastReturnedIx = ix;

        if (ix <= list.wrpStartIx || list.wrpEndIx <= ix) return list.cache.get(ix++);

        list.cacheNext();

        return list.cache.get(ix++);
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

        if (ix <= list.wrpStartIx || list.wrpEndIx < ix) return list.cache.get(--ix);

        list.cacheUntilIx(--ix);

        return list.cache.get(ix);
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
