package generated.org.springframework.boot.databases;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FlatTable<T> implements ITable<T> {

    public ITable<ITable<T>> tables;
    public int size;

    public Class<T> type;

    public FlatTable(ITable<ITable<T>> tables, Class<T> type) {
        this.tables = tables;
        this.size = -1;
        this.type = type;
    }

    public FlatTable(ITable<ITable<T>> tables, int size, Class<T> type) {
        this.tables = tables;
        this.size = size;
        this.type = type;
    }

    @Override
    public int size() {
        if (size != -1) return size;

        for (ITable<T> table : tables) {
            size += table.size();
        }

        return size;
    }

    class FlatIterator implements Iterator<T> {

        Iterator<ITable<T>> iters;
        Iterator<T> currIter;

        boolean reversed;

        public FlatIterator() {
            this(false);
        }

        public FlatIterator(boolean reversed) {
            this.iters = reversed ? tables.backIterator() : tables.iterator();
            this.currIter = null;
            this.reversed = reversed;
        }

        private void nextCurr() {
            ITable<T> next = iters.next();
            currIter = reversed ? next.backIterator() : next.iterator();
        }

        @Override
        public boolean hasNext() {
            if (currIter == null) {
                if (!iters.hasNext()) return false;
                nextCurr();
            }

            if (currIter.hasNext()) return true;

            if (iters.hasNext()) {
                nextCurr();
                return hasNext();
            }

            return false;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return currIter.next();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new FlatIterator();
    }

    @Override
    public Iterator<T> backIterator() {
        return new FlatIterator(true);
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public ITable<T> clone() {
        return new FlatTable<>(tables.clone(), size, type);
    }
}
