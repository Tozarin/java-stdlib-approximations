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

        if (ix < list.wrpStartIx || list.wrpEndIx <= ix) return list.cache.get(ix++);

        assert (ix == list.wrpStartIx);
        list.cacheNext();

        return list.cache.get(ix++);
    }
}
