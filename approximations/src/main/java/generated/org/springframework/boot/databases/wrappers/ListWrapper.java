package generated.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.IWrapper;
import generated.org.springframework.boot.databases.iterators.ListWrapperIterator;
import generated.org.springframework.boot.databases.iterators.ListWrapperListIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicList;

import java.lang.reflect.Array;
import java.util.*;

public class ListWrapper<T> implements List<T>, IWrapper<T> {

    public ITable<T> table;
    public int tblStartIx;
    public int tblEndIx;
    public Class<T> type;

    public Iterator<T> tblIter;
    public Iterator<T> backTblIter;

    public SymbolicList<T> cache;
    public int sizeOfCache;
    public int wrpStartIx;
    public int wrpEndIx;

    public int modCount;
    private boolean inited = false;

    public ListWrapper(ITable<T> table) {
        this.table = table;
    }

    private void ensureInited() {
        if (inited) return;

        int tableSize = table.size();
        this.tblStartIx = 0;
        this.tblEndIx = tableSize;
        this.type = table.type();

        this.tblIter = this.table.iterator();
        this.backTblIter = this.table.backIterator();

        this.cache = Engine.makeSymbolicList();
        Engine.assume(cache != null);
        Engine.assume(cache.size() == tableSize);
        this.sizeOfCache = tableSize;
        this.wrpStartIx = 0;
        this.wrpEndIx = tableSize;

        this.modCount = 0;
        this.inited = true;
    }

    public ListWrapper(
            ITable<T> table,
            int tblStartIx,
            int tblEndIx,
            Class<T> type,
            Iterator<T> tblIter,
            Iterator<T> backTblIter,
            SymbolicList<T> cache,
            int sizeOfCache,
            int wrpStartIx,
            int wrpEndIx,
            int modCount
    ) {
        this.table = table;
        this.tblStartIx = tblStartIx;
        this.tblEndIx = tblEndIx;
        this.type = type;
        this.tblIter = tblIter;
        this.backTblIter = backTblIter;
        this.cache = cache;
        this.sizeOfCache = sizeOfCache;
        this.wrpStartIx = wrpStartIx;
        this.wrpEndIx = wrpEndIx;
        this.modCount = modCount;
    }

    @Override
    public ITable<T> unwrap() {
        return table;
    }

    public void checkIndex(int ix) {
        if (ix < 0 || ix >= sizeOfCache) throw new IndexOutOfBoundsException();
    }

    private int findLeft(T t) {

        for (int i = 0; i < wrpStartIx; i++) {
            T cached = cache.get(i);
            if (cached == t || cached.equals(t)) return i;
        }

        return -1;
    }

    private int findLeftLast(T t) {

        for (int i = wrpStartIx - 1; i > -1; i--) {
            T cached = cache.get(i);
            if (cached == t || cached.equals(t)) return i;
        }

        return -1;
    }

    private int findRight(T t) {

        for (int i = wrpEndIx; i < sizeOfCache; i++) {
            T cached = cache.get(i);
            if (cached == t || cached.equals(t)) return i;
        }

        return -1;
    }

    private int findRightLast(T t) {

        for (int i = sizeOfCache - 1; i > wrpEndIx - 1; i--) {
            T cached = cache.get(i);
            if (cached == t || cached.equals(t)) return i;
        }

        return -1;
    }

    private int findMiddle(T t) {
        T cached = cacheUntil(t);
        if (cached != null) return wrpStartIx;
        return -1;
    }

    private int findMiddleLast(T t) {
        T cached = cacheUntilBack(t);
        if (cached != null) return wrpEndIx - 1;
        return -1;
    }

    public T cacheNext() {

        if (wrpStartIx == wrpEndIx) return null;
        T next = tblIter.next();
        cache.set(wrpStartIx, next);
        wrpStartIx++;
        tblStartIx++;
        return next;
    }

    public T cachePrev() {

        if (wrpStartIx == wrpEndIx) return null;
        T prev = backTblIter.next();
        cache.set(wrpEndIx - 1, prev);
        wrpEndIx--;
        tblEndIx--;
        return prev;
    }

    private T cacheUntil(T t) {

        T cached = cacheNext();
        while (cached != null && cached != t && !cached.equals(t)) {
            cached = cacheNext();
        }
        return cached;
    }

    private T cacheUntilBack(T t) {

        T cached = cachePrev();
        while (cached != null && cached != t && !cached.equals(t)) {
            cached = cachePrev();
        }
        return cached;
    }

    private void cacheUntilIx(int ix) {

        while (wrpStartIx <= ix && wrpStartIx < wrpEndIx) {
            cacheNext();
        }
    }

    private boolean removeLeft(T t) {

        int ix = findLeft(t);
        if (ix != -1) {
            cache.remove(ix);

            wrpStartIx--;
            wrpEndIx--;

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    private boolean removeRight(T t) {

        int ix = findRight(t);
        if (ix != -1) {
            cache.remove(ix);

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    private boolean removeMiddle(T t) {

        T cached = cacheUntil(t);

        if (cached != null) {
            cache.remove(wrpStartIx);

            wrpStartIx--;
            wrpEndIx--;

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    @Override
    public int size() {
        ensureInited();
        int count = 0;
        for (T ignored : this) { count++; }

        return count;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        ensureInited();
        return new ListWrapperIterator<>(this);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        ensureInited();

        Object[] a = new Object[sizeOfCache];

        int ix = 0;
        for (T t : this) a[ix++] = t;

        return a;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(@NotNull T1[] a) {
        ensureInited();

        Class<?> genericType = a.getClass().componentType();

        //if (!genericType.isAssignableFrom(type)) throw new ArrayStoreException();

        T1[] array = a.length < sizeOfCache ?
                (T1[]) Array.newInstance(genericType, sizeOfCache)
                : a;
        Iterator<T> iter = iterator();

        int ix = 0;
        while (iter.hasNext()) array[ix++] = (T1) iter.next();

        while (ix < a.length) array[ix++] = null;

        return array;
    }

    @Override
    public boolean add(T t) {
        ensureInited();

        cache.insert(sizeOfCache, t);
        sizeOfCache++;
        modCount++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        ensureInited();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        if (removeLeft(t)) return true;
        if (removeMiddle(t)) return true;

        return removeRight(t);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        ensureInited();

        for (Object o : c) if (!contains(o)) return false;

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        ensureInited();

        if (c.isEmpty()) return false;

        for (T t : c) cache.insert(sizeOfCache++, t);
        modCount++;

        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        ensureInited();

        checkIndex(index);

        int sizeOfCol = c.size();
        if (sizeOfCol == 0) return false;

        cacheUntilIx(index - 1);
        for (T t : c) cache.insert(index++, t);

        wrpStartIx += sizeOfCol;
        wrpEndIx += sizeOfCol;

        sizeOfCache += sizeOfCol;
        modCount++;

        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        ensureInited();

        if (c.isEmpty()) return false;

        boolean isChanged = false;
        for (Object o : c) isChanged |= remove(o);

        if (isChanged) modCount++;

        return isChanged;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        ensureInited();

        SymbolicList<T> newCache = Engine.makeSymbolicList();
        int newSize = 0;
        Engine.assume(newCache != null);
        Engine.assume(newCache.size() == 0);

        cacheUntilIx(wrpEndIx);
        for (int i = 0; i < sizeOfCache; i++) {
            T t = cache.get(i);
            if (c.contains(t)) newCache.insert(newSize++, t);
        }

        if (newSize == sizeOfCache) return false;

        cache = newCache;
        sizeOfCache = newSize;

        wrpStartIx = -1;
        wrpEndIx = -1;
        tblStartIx = -1;
        tblEndIx = -1;
        modCount++;

        return true;
    }

    @Override
    public void clear() {

        cache = Engine.makeSymbolicList();
        Engine.assume(cache != null);
        Engine.assume(cache.size() == 0);
        sizeOfCache = 0;

        wrpStartIx = -1;
        wrpEndIx = -1;
        tblStartIx = -1;
        tblEndIx = -1;
        modCount++;
    }

    @Override
    public T get(int index) {
        ensureInited();

        checkIndex(index);

        if (index < wrpStartIx || wrpEndIx <= index) return cache.get(index);

        cacheUntilIx(index);

        return cache.get(index);
    }

    @Override
    public T set(int index, T element) {
        ensureInited();

        checkIndex(index);

        T prev = get(index);
        cache.set(index, element);
        modCount++;

        return prev;
    }

    @Override
    public void add(int index, T element) {
        ensureInited();

        checkIndex(index);

        if (index < wrpStartIx) {
            wrpStartIx++;
            wrpEndIx++;
        } else if (index < wrpEndIx) {
            cacheUntilIx(index);
            wrpStartIx++;
            wrpEndIx++;
        }

        cache.insert(index, element);
        sizeOfCache++;
        modCount++;
    }

    @Override
    public T remove(int index) {
        ensureInited();

        checkIndex(index);

        if (index < wrpStartIx) {
            wrpStartIx--;
            wrpEndIx--;
        } else if (index < wrpEndIx) {
            cacheUntilIx(index);
            wrpStartIx--;
            wrpEndIx--;
        }

        T prev = cache.get(index);
        cache.remove(index);
        sizeOfCache--;
        modCount++;

        return prev;
    }

    @Override
    public int indexOf(Object o) {
        ensureInited();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        int leftIx = findLeft(t);
        if (leftIx != -1) return leftIx;

        int middleIx = findMiddle(t);
        if (middleIx != -1) return middleIx;

        return findRight(t);
    }

    @Override
    public int lastIndexOf(Object o) {
        ensureInited();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        int rightIx = findRightLast(t);
        if (rightIx != -1) return rightIx;

        int middleIx = findMiddleLast(t);
        if (middleIx != -1) return middleIx;

        return findLeftLast(t);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        ensureInited();
        return new ListWrapperListIterator<>(this, 0);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        ensureInited();
        return new ListWrapperListIterator<>(this, index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ensureInited();

        if (fromIndex < 0 || toIndex > sizeOfCache || fromIndex > toIndex) throw new IndexOutOfBoundsException();

        int newSize = toIndex - fromIndex;

        SymbolicList<T> newCache = Engine.makeSymbolicList();
        Engine.assume(newCache != null);
        cache.copy(newCache, fromIndex, 0, newSize);

        int startDif = wrpStartIx - fromIndex;
        int endDif = wrpEndIx - toIndex;

        int newWrpStartIx = Math.max(startDif, 0);
        int newWrpEndIx = endDif < 0 ? newSize - endDif : newSize;

        int newTblStartIx = startDif < 0 ? tblStartIx + startDif : tblStartIx;
        int newTblEndIx = endDif < 0 ? tblEndIx : tblEndIx - endDif;

        Iterator<T> newTblIter = table.iterator();
        for (int i = 0; i < tblStartIx; i++) newTblIter.next();

        Iterator<T> newBackTblIter = table.backIterator();
        for (int i = sizeOfCache - 1; tblEndIx <= i; i--) newBackTblIter.next();

        return new ListWrapper<>(
                table,
                newTblStartIx,
                newTblEndIx,
                type,
                newTblIter,
                newBackTblIter,
                newCache,
                newSize,
                newWrpStartIx,
                newWrpEndIx,
                0
        );
    }

    public T first() {
        return table.first();
    }
}
