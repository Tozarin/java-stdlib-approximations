package generated.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.iterators.wrappers.ListWrapperIterator;
import generated.org.springframework.boot.databases.iterators.wrappers.ListWrapperListIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicList;

import java.lang.reflect.Array;
import java.util.*;

public class ListWrapper<T> implements List<T>, IWrapper<T> {

    public ITable<T> table;
    public Iterator<T> tblIter;
    public int tblStartIx;
    public Class<T> type;

    public SymbolicList<T> cache;
    public int sizeOfCache;
    public int wrpStartIx;
    public int wrpEndIx;

    public int modCount;
    private boolean initialized = false;

    public ListWrapper(ITable<T> table) { this.table = table; }

    // table, * - cached, 0 - uncached
    // |0|1|...|tblStIx-1|tblStIx|tblStIx+1|...|size-1|
    // |*|*|...|    *    |   *   |    0    |...|  0   |

    // list, * - cached from table: wrpStIx points tblStIx, 0 - uncached, $ - added to end of list by add()
    // |0|1|...|wrpStIx-1|wrpStIx|wrpStIx+1|...|wrpEndIx-1|wrpEndIx|wrpEndIx+1|...|sizeOfCache-1|
    // |*|*|...|    *    |   *   |    0    |...|     0    |    $   |     $    |...|      $      |

    private void ensureInitialized() {
        if (initialized) return;

        int tblSize = table.size();
        this.tblIter = table.iterator();
        this.tblStartIx = 0;
        this.type = table.type();

        this.cache = Engine.makeFullySymbolicList();
        Engine.assume(cache != null);
        Engine.assume(cache.size() == tblSize);
        this.sizeOfCache = tblSize;
        this.wrpStartIx = 0;
        this.wrpEndIx = tblSize;

        this.modCount = 0;
        this.initialized = true;
    }

    // region Cache

    public T cacheNext() {
        if (wrpStartIx == wrpEndIx) return null;

        T next = tblIter.next();
        cache.set(wrpStartIx, next);

        wrpStartIx++;
        tblStartIx++;

        return next;
    }

    public void cacheUntilIx(int ix) {

        while (wrpStartIx <= ix && wrpStartIx < wrpEndIx) {
            cacheNext();
        }
    }

    // endregion

    public void checkIndex(int ix) {
        if (ix < 0 || ix >= sizeOfCache) throw new IndexOutOfBoundsException();
    }

    // region Find

    private int findLeft(T t) {

        for (int i = 0; i < wrpStartIx; i++) {
            T cached = cache.get(i);
            if (cached.equals(t)) return i;
        }

        return -1;
    }

    private int findMiddle(T t) {
        T next = cacheNext();
        if (next == null) return -1;

        if (next.equals(t)) return wrpStartIx;

        return findMiddle(t);
    }

    private int findRight(T t) {

        for (int i = wrpEndIx; i < sizeOfCache; i++) {
            T cached = cache.get(i);
            if (cached.equals(t)) return i;
        }

        return -1;
    }

    // endregion

    // region Remove

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

    private boolean removeMiddle(T t) {

        int ix = findMiddle(t);

        if (ix != -1) {
            cache.remove(ix);

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

    // endregion

    @Override
    public ITable<T> unwrap() { return table; }

    private int cachedSize = -1;
    @Override
    public int size() {
        ensureInitialized();
        if (cachedSize != -1) return cachedSize;

        int count = 0;
        for (T ignored : this) count++;

        cachedSize = count;
        return count;
    }

    @Override
    public boolean isEmpty() { return size() == 0; }

    @Override
    public boolean contains(Object o) {
        ensureInitialized();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        int leftIx = findLeft(t);
        if (leftIx != -1) return true;

        int midIx = findMiddle(t);
        if (midIx != -1) return true;

        return findRight(t) != -1;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        ensureInitialized();
        return new ListWrapperIterator<>(this);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        ensureInitialized();

        Object[] arr = new Object[sizeOfCache];

        int ix = 0;
        for (T t : this) arr[ix++] = t;

        return arr;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(@NotNull T1[] a) {
        ensureInitialized();

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
        ensureInitialized();

        cache.insert(sizeOfCache, t);
        sizeOfCache++;
        modCount++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        ensureInitialized();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        if (removeLeft(t)) return true;
        if (removeMiddle(t)) return true;

        return removeRight(t);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        ensureInitialized();

        for (Object o : c) if (!contains(o)) return false;

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        ensureInitialized();

        if (c.isEmpty()) return false;

        for (T t : c) cache.insert(sizeOfCache++, t);
        modCount++;

        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        ensureInitialized();

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
        ensureInitialized();

        if (c.isEmpty()) return false;

        boolean isChanged = false;
        for (Object o : c) isChanged |= remove(o);

        if (isChanged) modCount++;

        return isChanged;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        ensureInitialized();

        SymbolicList<T> newCache = Engine.makeSymbolicList();
        int newSize = 0;
        Engine.assume(newCache != null);
        Engine.assume(newCache.size() == 0);

        cacheUntilIx(wrpEndIx);
        for (T t : this) {
            if (c.contains(t)) newCache.insert(newSize++, t);
        }

        if (newSize == sizeOfCache) return false;

        cache = newCache;
        sizeOfCache = newSize;

        wrpStartIx = -1;
        wrpEndIx = -1;
        tblStartIx = -1;
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
        modCount++;
    }

    @Override
    public T get(int index) {
        ensureInitialized();

        checkIndex(index);

        if (index < wrpStartIx || wrpEndIx <= index) return cache.get(index);

        cacheUntilIx(index);

        return cache.get(index);
    }

    @Override
    public T set(int index, T element) {
        ensureInitialized();

        checkIndex(index);

        T prev = get(index);
        cache.set(index, element);
        modCount++;

        return prev;
    }

    @Override
    public void add(int index, T element) {
        ensureInitialized();

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
        ensureInitialized();

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
        ensureInitialized();

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
        ensureInitialized();

        if (o == null) throw new NullPointerException();
        if (!type.isInstance(o)) throw new ClassCastException();

        T t = type.cast(o);

        cacheUntilIx(wrpEndIx);

        for (int i = sizeOfCache - 1; i > -1; i--) {
            if (cache.get(i).equals(t)) return i;
        }

        return -1;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        ensureInitialized();
        return new ListWrapperListIterator<>(this, 0);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        ensureInitialized();
        return new ListWrapperListIterator<>(this, index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ensureInitialized();

        if (fromIndex < 0 || toIndex > sizeOfCache || fromIndex > toIndex) throw new IndexOutOfBoundsException();

        List<T> subList = new ArrayList<>();
        cacheUntilIx(toIndex - 1);

        int newSize = 0;
        for (int i = fromIndex; i < toIndex; i++) subList.add(get(i));

        return subList;
    }
}
