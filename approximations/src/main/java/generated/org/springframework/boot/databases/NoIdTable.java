package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NoIdTable implements ITable<Object[]> {

    public Object[][] data;
    public int size;

    public int columnCount;

    public NoIdTable(Class<?>... columnTypes) {

        this.columnCount = columnTypes.length;

        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);

        this.data = new Object[columnCount][];

        for (int i = 0; i < columnCount; i++) {
            data[i] = Engine.makeSymbolicArray(columnTypes[i], size);
            Engine.assume(data[i] != null);
        }
    }

    public NoIdTable(
            Object[][] data,
            int size,
            int columnCount
    ) {
        this.data = data;
        this.size = size;
        this.columnCount = columnCount;
    }

    @Override
    public int size() {
        return size;
    }

    public Object[] getRow(int ix) {

        Object[] row = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            row[i] = data[i][ix];
        }

        return row;
    }

    class NoIdIterator implements Iterator<Object[]> {

        int ix;
        int endIx;

        public NoIdIterator() {
            this.ix = 0;
            this.endIx = size;
        }

        @Override
        public boolean hasNext() {
            return ix < endIx;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            return getRow(ix++);
        }
    }

    class NoIdBackIterator implements Iterator<Object[]> {

        int ix;

        public NoIdBackIterator() {
            this.ix = size - 1;
        }

        @Override
        public boolean hasNext() {
            return 0 <= ix;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            return getRow(ix--);
        }
    }

    @Override
    public Iterator<Object[]> iterator() {
        return new NoIdIterator();
    }

    @Override
    public Iterator<Object[]> backIterator() {
        return new NoIdBackIterator();
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    @Override
    public ITable<Object[]> clone() {
        return new NoIdTable(
                data,
                size,
                columnCount
        );
    }
}
