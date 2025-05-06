package generated.org.springframework.boot.databases;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class SortedTable<T, R> implements ITable<T> {

    public ITable<T> table;
    public int size;
    public int limit; // -1 if no limit
    public int offset; // 0 if no offset
    public boolean direction; // true -  ASC, false - DESC
    public boolean nulls; // true - LAST, false - FIRST

    public int tblSize;

    public BiFunction<T, Object[], R> translate;
    public BiFunction<R, R, Integer> comparer;

    public Object[] sorted;

    // arguments of original repository method
    Object[] methodArgs;

    @SuppressWarnings("unchecked")
    public SortedTable(
            ITable<T> table,
            int limit,
            int offset,
            boolean direction,
            boolean nulls,
            BiFunction<T, Object[], R> translate,
            BiFunction<R, R, Integer> comparer,
            Object[] methodArgs
    ) {

        this.table = table;
        this.translate = translate;
        this.comparer = comparer;
        this.limit = limit;
        this.offset = offset;
        this.direction = direction;
        this.nulls = nulls;
        this.methodArgs = methodArgs;

        this.tblSize = table.size();

        if (limit == -1) {
            this.size = tblSize - offset;
        } else {
            this.size = Math.min(tblSize - offset, limit);
        }

        //this.sorted = (T[]) Array.newInstance(table.type(), tblSize);
        this.sorted = new Object[tblSize];
        Iterator<T> tblIter = table.iterator();
        int ix = 0;
        while (tblIter.hasNext()) {
            sorted[ix++] = tblIter.next();
        }
        Sort();
    }

    public SortedTable(
            ITable<T> table,
            int limit,
            int offset,
            boolean direction,
            boolean nulls,
            BiFunction<T, Object[], R> translate,
            BiFunction<R, R, Integer> comparer
    ) {
        this(table, limit, offset, direction, nulls, translate, comparer, new Object[0]);
    }

    public SortedTable(
            ITable<T> table,
            int limit,
            int offset,
            int size,
            boolean direction,
            boolean nulls,
            int tblSize,
            BiFunction<T, Object[], R> translate,
            BiFunction<R, R, Integer> comparer,
            Object[] methodArgs,
            T[] sorted
    ) {
        this.table = table;
        this.limit = limit;
        this.offset = offset;
        this.size = size;
        this.direction = direction;
        this.nulls = nulls;
        this.tblSize = tblSize;
        this.translate = translate;
        this.comparer = comparer;
        this.methodArgs = methodArgs;
        this.sorted = sorted;
    }

    public boolean compare(R left, R right) {
        if (left == null) return nulls; // NULLs always bigger or else
        boolean common = comparer.apply(left, right) > 0;
        return common == direction;
    }

    public R applyTranslate(T t) {
        return translate.apply(t, methodArgs);
    }

    // bubble sort
    @SuppressWarnings("unchecked")
    public void Sort() {
        for (int i = 0; i < tblSize; i++) {
            boolean swapped = false;
            for (int j = 0; j < tblSize - i - 1; j++) {

                R left = applyTranslate((T) sorted[j]);
                R right = applyTranslate((T) sorted[j + 1]);

                if (compare(left, right)) {

                    T tmp = (T) sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = tmp;

                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    @Override
    public int size() {
        return size;
    }

    class SortedIterator implements Iterator<T> {

        int ix;
        int endIx;

        public SortedIterator() {
            this.ix = offset;
            this.endIx = size;
        }

        @Override
        public boolean hasNext() {
            return ix < endIx;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return (T) sorted[ix++];
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SortedIterator();
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public T first() {
        Iterator<T> iter = iterator();
        if (!iter.hasNext()) return null;
        return iter.next();
    }
}
