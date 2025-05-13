package generated.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.ITable;
import generated.org.springframework.boot.databases.iterators.wrappers.ListWrapperIterator;
import generated.org.springframework.boot.databases.iterators.wrappers.ListWrapperListIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public ListWrapper(ITable<T> table) {
        this.table = table;
    }

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
        this.tblStartIx = -1;
        this.type = table.type();

        this.cache = Engine.makeFullySymbolicList();
        Engine.assume(cache != null);
        Engine.assume(cache.size() == tblSize);
        this.sizeOfCache = tblSize;
        this.wrpStartIx = -1;
        this.wrpEndIx = tblSize;

        this.modCount = 0;
        this.initialized = true;
    }

    // region Cache

    public T cacheNext() {
        // table doesnt contain next element
        if (wrpStartIx + 1 == wrpEndIx) return null;

        wrpStartIx++; // for example, -1 -> 0 (cache element at index 0)
        tblStartIx++;

        T next = tblIter.next();
        cache.set(wrpStartIx, next);

        return next;
    }

    public void cacheUntilIx(int ix) {
        // to cache element at index 2 we need to stop when wrpStartIx == 2 (this element cached)
        while (wrpStartIx < ix) {

            // == null mean that we cached all elements
            if (cacheNext() == null) return;
        }
    }

    // endregion

    public void checkIndex(int ix) {
        if (ix < 0 || ix >= sizeOfCache) throw new IndexOutOfBoundsException();
    }

    // region Find

    // find in cache
    private int findLeft(T t) {
        for (int i = 0; i <= wrpStartIx; i++) {
            T cached = cache.get(i);
            if (cached.equals(t)) return i;
        }

        return -1;
    }

    // find in uncached part
    private int findMiddle(T t) {
        // cache next element
        T next = cacheNext();

        // all elements are cached
        if (next == null) return -1;

        // next is element at index wrpStartIx
        if (next.equals(t)) return wrpStartIx;

        return findMiddle(t);
    }

    // find in added part
    private int findRight(T t) {
        for (int i = wrpEndIx; i < sizeOfCache; i++) {
            T cached = cache.get(i);
            if (cached.equals(t)) return i;
        }

        return -1;
    }

    // endregion

    // region Remove

    // remove from cached part
    private boolean removeLeft(T t) {
        int ix = findLeft(t);
        if (ix != -1) {
            cache.remove(ix);

            // we need to move window of uncached elements in wrapper to left
            wrpStartIx--;
            wrpEndIx--;

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    // remove from uncached part
    private boolean removeMiddle(T t) {
        // here we cached to ix index (wrpStartIx == ix)
        int ix = findMiddle(t);
        if (ix != -1) {
            cache.remove(ix);

            // here we removed from cached part
            wrpStartIx--;
            wrpEndIx--;

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    // remove from added elements
    private boolean removeRight(T t) {
        int ix = findRight(t);
        if (ix != -1) {
            cache.remove(ix);

            // position of window of uncached elements has not changed

            sizeOfCache--;
            modCount++;

            return true;
        }

        return false;
    }

    // endregion

    @Override
    public ITable<T> unwrap() {
        return table;
    }

    @Override
    public int size() {
        ensureInitialized();

        // we need to force eval of all elements so size is correct
        int count = 0;
        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            T ignored = iter.next();
            count++;
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

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
        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            T t = iter.next();
            arr[ix++] = t;
        }

        return arr;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(@NotNull T1[] a) {
        ensureInitialized();

        Class<?> genericType = a.getClass().componentType();

        T1[] array = a.length < sizeOfCache ?
                (T1[]) Array.newInstance(genericType, sizeOfCache)
                : a;
        Iterator<T> iter = iterator();

        int ix = 0;
        while (iter.hasNext()) array[ix++] = (T1) iter.next();

        // corresponds semantics of toArray
        while (ix < a.length) array[ix++] = null;

        return array;
    }

    @Override
    public boolean add(T t) {
        ensureInitialized();

        // add to added part
        // t is last element now
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

        Iterator<?> iter = c.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        ensureInitialized();

        if (c.isEmpty()) return false;

        Iterator<? extends T> iter = c.iterator();
        while (iter.hasNext()) {
            add(iter.next());
        }
        modCount++;

        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        ensureInitialized();

        // throws errors
        checkIndex(index);

        int sizeOfCol = c.size();
        if (sizeOfCol == 0) return false;

        // cache until index-1, index is uncached (if index has not been cached)
        cacheUntilIx(index - 1);
        Iterator<? extends T> iter = c.iterator();
        while (iter.hasNext()) {
            T t = iter.next();
            cache.insert(index++, t);
        }

        // |0|1|...|index|index+1| ...  ->  |0|1|...|index|index+1| ... |index+sizeOfCol|  ...
        // |*|*|...|  *i |  *j   | ...      |*|*|...|  c0 |   c1  | ... |      *i       |  *j   | ...

        // |0|1|...|index-1|index| ...  ->  |0|1|...|index-1|index| ... |wrpStIndex|  ...
        // |*|*|...|   0   |  0  | ...      |*|*|...|   *   |  c0 | ... |  c_last  |  0  | ...

        // move window
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
        Iterator<?> iter = c.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            isChanged |= remove(o);
        }

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

        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            T t = iter.next();
            if (c.contains(t)) newCache.insert(newSize++, t);
        }

        // list does not change
        if (newSize == sizeOfCache) return false;

        cache = newCache;
        sizeOfCache = newSize;

        wrpStartIx = newSize - 1; // mean cached all
        wrpEndIx = newSize; // mean cached all
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
        wrpEndIx = 0;
        tblStartIx = -1;
        modCount++;
    }

    @Override
    public T get(int index) {
        ensureInitialized();

        checkIndex(index);

        // not in uncached part
        if (index <= wrpStartIx || wrpEndIx <= index) return cache.get(index);

        // cache to index
        cacheUntilIx(index);

        // wrpStIndex == index
        return cache.get(index);
    }

    @Override
    public T set(int index, T element) {
        ensureInitialized();

        checkIndex(index);

        T prev = get(index); // cached to index
        cache.set(index, element); // not rewrite uncached yet values because index is cached
        modCount++;

        return prev;
    }

    @Override
    public void add(int index, T element) {
        ensureInitialized();

        checkIndex(index);

        if (index <= wrpStartIx) {
            // updates cached part, need to move window
            wrpStartIx++;
            wrpEndIx++;
        } else if (index < wrpEndIx) {
            // to modify need cache elements to index because we can override values later
            // need to move window
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

        // as add
        if (index <= wrpStartIx) {
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

        // cache all
        cacheUntilIx(wrpEndIx);

        for (int i = sizeOfCache - 1; i >= 0; i--) {
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

        for (int i = fromIndex; i < toIndex; i++) subList.add(get(i));

        return subList;
    }

    public T first() {
        Iterator<T> iter = iterator();
        return iter.hasNext() ? iter.next() : null;
    }
}
