package generated.org.springframework.boot.databases;

import kotlin.jvm.functions.Function2;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedTable<T, R> implements ITable<T> {

    public ITable<T> table;
    public int size;
    public int limit; // -1 if no limit
    public int offset; // 0 if no offset
    public boolean direction; // true -  ASC, false - DESC
    public boolean nulls; // true - LAST, false - FIRST

    public int tblSize;

    public Function2<T, Object[], R> translate;
    public Function2<R, R, Integer> comparer;

    public T[] sorted;

    // arguments of original repository method
    Object[] methodArgs;

    @SuppressWarnings("unchecked")
    public SortedTable(
            ITable<T> table,
            int limit,
            int offset,
            boolean direction,
            boolean nulls,
            Function2<T, Object[], R> translate,
            Function2<R, R, Integer> comparer,
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

        this.sorted = (T[]) Array.newInstance(table.type(), tblSize);
        Iterator<T> tblIter = table.clone().iterator();
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
            Function2<T, Object[], R> translate,
            Function2<R, R, Integer> comparer
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
            Function2<T, Object[], R> translate,
            Function2<R, R, Integer> comparer,
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
        boolean common = comparer.invoke(left, right) > 0;
        return common == direction;
    }

    public R applyTranslate(T t) {
        return translate.invoke(t, methodArgs);
    }

    // bubble sort
    public void Sort() {
        for (int i = 0; i < tblSize; i++) {
            boolean swapped = false;
            for (int j = 0; j < tblSize - i - 1; j++) {

                R left = applyTranslate(sorted[j]);
                R right = applyTranslate(sorted[j + 1]);

                if (compare(left, right)) {

                    T tmp = sorted[j];
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
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return sorted[ix++];
        }
    }

    class SortedBackIterator implements Iterator<T> {

        int ix;

        public SortedBackIterator() {
            this.ix = size - 1;
        }

        @Override
        public boolean hasNext() {
            return offset <= ix;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return sorted[ix--];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SortedIterator();
    }

    @Override
    public Iterator<T> backIterator() {
        return new SortedBackIterator();
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public ITable<T> clone() {
        return new SortedTable<>(
                table.clone(),
                limit,
                offset,
                size,
                direction,
                nulls,
                tblSize,
                translate,
                comparer,
                methodArgs,
                sorted
        );
    }

    public static Integer stringComparer(String left, String right) {
        return left.compareTo(right);
    }
}
