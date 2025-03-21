package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.NoIdIterator;
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

    @Override
    public Iterator<Object[]> iterator() {
        return new NoIdIterator(this);
    }

    @Override
    public Iterator<Object[]> backIterator() {
        return new NoIdIterator(this, true);
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    @Override
    public Object[] first() {
        if (size == 0) return null;
        return getRow(0);
    }
}
