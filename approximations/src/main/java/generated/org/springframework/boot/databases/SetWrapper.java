package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.lang.reflect.Array;
import java.util.*;

public class SetWrapper<T> implements Set<T> {

    public ITable<T> table;
    public int sizeOfTable;
    public int ptr;
    public Class<T> type;

    public Set<T> cache;
    public Set<T> removedCache;
    public int sizeOfCache;

    public int modCount;

    public SetWrapper(ITable<T> table) {

        this.table = table.clone();
        this.sizeOfTable = table.size();
        this.ptr = 0;
        this.type = table.type();

        this.cache = new HashSet<>();
        this.removedCache = new HashSet<>();
        this.sizeOfCache = 0;

        this.modCount = 0;
    }

    public boolean cacheNext() {

        if (ptr == sizeOfTable) return false;

        T t = table.getEnsure(ptr);
        ptr++;

        if (removedCache.contains(t)) return false;

        if (cache.add(t)) {
            sizeOfCache++;
            return true;
        }

        return false;
    }

    public boolean cacheUntilCached() {

        if (ptr == sizeOfTable) return false;
        if (cacheNext()) return true;

        return cacheUntilCached();
    }

    @Override
    public int size() {

        while (ptr < sizeOfTable) {
            cacheNext();
        }

        return sizeOfCache;
    }

    @Override
    public boolean isEmpty() {

        if (ptr == sizeOfTable) {
            return sizeOfCache == 0;
        }

        return cacheUntilCached();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {

        if (removedCache.contains(o)) return false;
        if (cache.contains(o)) return true;

        if (ptr == sizeOfTable) return false;

//        if (o == null) throw new NullPointerException();
//        if (!type.isInstance(o)) throw new ClassCastException();
//
//        T t = type.cast(o);

        return table.containsIn((T) o, ptr, sizeOfTable);
    }

    class SetWrapperIterator implements Iterator<T> {

        public T curr;
        public Iterator<T> cacheIter;

        public int expectedModCount;

        public SetWrapperIterator() {

            this.curr = null;
            this.cacheIter = cache.iterator();

            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            if (!cacheIter.hasNext()) {

                if (cacheUntilCached()) {
                    curr = table.getEnsure(ptr - 1);
                    return true;
                }

                return false;
            };

            curr = cacheIter.next();

            return true;
        }

        @Override
        public T next() {

            if (!hasNext()) throw new NoSuchElementException();
            if (expectedModCount != modCount) throw new ConcurrentModificationException();

            T tmp = curr;
            curr = null;

            return tmp;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SetWrapperIterator();
    }

    @Override
    public Object[] toArray() {

        Object[] a = new Object[size()];

        int ix = 0;
        for (T t : this) a[ix++] = t;

        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {

        Class<?> genericType = a.getClass().componentType();

        //if (!genericType.isAssignableFrom(type)) throw new ArrayStoreException();

        T1[] array = a.length < size() ?
                (T1[]) Array.newInstance(genericType, size())
                : a
                ;
        Iterator<T> iter = iterator();

        int ix = 0;
        while (iter.hasNext()) array[ix++] = (T1) iter.next();

        while (ix < a.length) array[ix++] = null;

        return array;
    }

    @Override
    public boolean add(T t) {

        boolean c = contains(t);
        if (!c) modCount++;

        removedCache.remove(t);
        cache.add(t);

        return !c;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {

        boolean c = contains((T) o);
        if (c) modCount++;

        cache.remove(o);
        removedCache.add((T) o);

        return c;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        for (Object o : c) if (!contains(o)) return false;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

        if (c.isEmpty()) return false;

        boolean isChanged = false;
        for (T t : c) isChanged |= add(t);

        if (isChanged) modCount++;

        return isChanged;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean retainAll(Collection<?> c) {

        Set<T> newCache = new HashSet<>();
        int sizeOfNewCache = 0;

        for (Object o : c) {
            if (contains(o)) {
                newCache.add((T) o);
                sizeOfNewCache++;
            };
        }

        boolean isChanged = false;
        if (sizeOfNewCache == sizeOfCache) {
            isChanged = cacheUntilCached();
        }

        if (isChanged) modCount++;

        cache = newCache;
        removedCache.clear();
        ptr = sizeOfTable;
        sizeOfCache = sizeOfNewCache;

        return isChanged;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        if (c.isEmpty()) return false;

        boolean isChanged = false;
        for (Object o : c) isChanged |= remove(o);

        if (isChanged) modCount++;

        return isChanged;
    }

    @Override
    public void clear() {

        cache.clear();
        removedCache.clear();
        sizeOfCache = 0;
        ptr = sizeOfCache;
        modCount++;
    }
}
