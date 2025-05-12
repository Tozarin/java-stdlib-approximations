package generated.org.springframework.boot.databases.iterators.wrappers;

import generated.org.springframework.boot.databases.wrappers.IListWrapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListWrapperIterator<T> implements Iterator<T> {

    IListWrapper<T> list;

    public int ix;
    public int count;

    public int expectedModCount;

    public ListWrapperIterator(IListWrapper<T> list) {
        this.list = list;
        this.ix = 0;
        this.count = list.getSizeOfCache();
        this.expectedModCount = list.getModCount();
    }

    @Override
    public boolean hasNext() {
        return ix < count;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        if (expectedModCount != list.getModCount()) throw new ConcurrentModificationException();

        if (ix < list.getWrpStartIx() || list.getWrpEndIx() <= ix) return list.getFromCache(ix++);

        assert (ix == list.getWrpStartIx());
        list.cacheNext();

        return list.getFromCache(ix++);
    }
}
