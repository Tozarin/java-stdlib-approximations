package generated.org.springframework.boot.databases.iterators.wrappers;

import generated.org.springframework.boot.databases.wrappers.SetWrapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SetWrapperIterator<T> implements Iterator<T> {

    SetWrapper<T> set;
    Iterator<T> tblIter;
    Iterator<T> cacheIter;
    T curr;

    int expectedModCount;

    public SetWrapperIterator(SetWrapper<T> set) {
        this.set = set;
        this.tblIter = set.table.iterator();
        for (int i = 0; i < set.ptr; i++) {
            tblIter.next();
        }

        this.cacheIter = set.cache.iterator();
        this.curr = null;
        this.expectedModCount = set.modCount;
    }

    @Override
    public boolean hasNext() {

        if (curr != null) return true;

        if (cacheIter.hasNext()) {
            T candidate = cacheIter.next();
            if (set.removedCache.contains(candidate)) {
                return hasNext();
            }

            curr = candidate;
            return true;
        }

        if (tblIter.hasNext()) {
            T candidate = tblIter.next();
            if (set.removedCache.contains(candidate) || set.cache.contains(candidate)) {
                return hasNext();
            }

            curr = candidate;
            return true;
        }

        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        if (expectedModCount != set.modCount) throw new ConcurrentModificationException();

        T tmp = curr;
        curr = null;

        return tmp;
    }
}
