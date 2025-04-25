package generated.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.iterators.wrappers.SetWrapperIterator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetWrapper<T> implements Set<T>, IWrapper<T> {

    public ITable<T> table;
    public Iterator<T> tblIter;
    public int sizeOfTable = -1;
    public int ptr;
    public Class<T> type;

    public Set<T> cache;
    public Set<T> removedCache;
    public int sizeOfCache;

    public int modCount;

    public SetWrapper(ITable<T> table) {

        this.table = table;
        this.tblIter = this.table.iterator();
        this.ptr = 0;
        this.type = table.type();

        this.cache = new HashSet<>();
        this.removedCache = new HashSet<>();
        this.sizeOfCache = 0;

        this.modCount = 0;
    }

    private int getSizeOfTable() {
        if (sizeOfTable == -1) {
            sizeOfTable = table.size();
        }

        return sizeOfTable;
    }

    @Override
    public ITable<T> unwrap() {
        return table;
    }

    public T cacheNext() {

        if (ptr == getSizeOfTable()) return null;

        T t = tblIter.next();
        ptr++;

        if (removedCache.contains(t)) return null;

        if (cache.add(t)) {
            sizeOfCache++;
            return t;
        }

        return null;
    }

    public T cacheUntil(T t) {

        T cached = cacheNext();
        while (cached != null && cached != t && !cached.equals(t)) {
            cached = cacheNext();
        }
        return cached;
    }

    public boolean cacheUntilCached() {

        if (ptr == getSizeOfTable()) return false;
        if (cacheNext() != null) return true;

        return cacheUntilCached();
    }

    @Override
    public int size() {

        int count = 0;
        for (T ignored : this) { count++; }

        return count;
    }

    @Override
    public boolean isEmpty() {

        if (ptr == getSizeOfTable()) {
            return sizeOfCache == 0;
        }

        return cacheUntilCached();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {

        if (removedCache.contains(o)) return false;
        if (cache.contains(o)) return true;

        if (ptr == getSizeOfTable()) return false;

        return cacheUntil((T) o) != null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SetWrapperIterator<>(this);
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
                : a;
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
            }
        }

        boolean isChanged = false;
        if (sizeOfNewCache == sizeOfCache) {
            isChanged = cacheUntilCached();
        }

        if (isChanged) modCount++;

        cache = newCache;
        removedCache.clear();
        ptr = getSizeOfTable();
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
