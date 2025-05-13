package generated.org.springframework.boot.databases.iterators.wrappers;

import generated.org.springframework.boot.databases.wrappers.ListWrapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListWrapperIterator<T> implements Iterator<T> {

    ListWrapper<T> list;

    public int ix;
    public int count;

    public int expectedModCount;

    public ListWrapperIterator(ListWrapper<T> list) {
        this.list = list;
        this.ix = 0;
        this.count = list.sizeOfCache;
        this.expectedModCount = list.modCount;
    }

    @Override
    public boolean hasNext() {
        return ix < count;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        if (expectedModCount != list.modCount) throw new ConcurrentModificationException();

        // we are in cached or added part of wrapper
        if (ix <= list.wrpStartIx || list.wrpEndIx <= ix) list.cache.get(ix++);

        // we need to cache next element
        // here ix == list.wrpStartIx + 1
        list.cacheNext();

        return list.cache.get(ix++);
    }
}
