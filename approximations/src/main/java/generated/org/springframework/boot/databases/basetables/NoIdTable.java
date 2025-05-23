package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.NoIdIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;

// Table for many-to-many relation
public class NoIdTable extends ANoIdTable {

    // values always null
    // this is just set
    public Object[][] data;
    public int size;

    public Class<?>[] columnTypes;
    public int columnCount;

    public NoIdTable(Class<?>... columnTypes) {

        this.columnTypes = columnTypes;
        this.columnCount = columnTypes.length;

        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);

        this.data = new Object[columnCount][];
        for (int i = 0; i < columnCount; i++) {
            data[i] = Engine.makeSymbolicArray(columnTypes[i], size);
        }
    }

    public NoIdTable(
            Object[][] data,
            int size,
            Class<?>[] columnTypes,
            int columnCount
    ) {
        this.data = data;
        this.size = size;
        this.columnTypes = columnTypes;
        this.columnCount = columnCount;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int columnCount() {
        return columnCount;
    }

    @Override
    public Class<?>[] columnTypes() {
        return columnTypes;
    }

    @Override
    @NotNull
    public Iterator<Object[]> iterator() {
        return new NoIdIterator(this);
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    public Object[] collectRow(int ix) {
        Object[] row = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            row[i] = data[i][ix];
        }
        return row;
    }

    public void ensureRow(Object[] row) {
        Engine.assume(row != null);
        Engine.assume(row.length == columnCount);

        for (int i = 0; i < columnCount; i++) {
            Engine.assume(row[i] != null);
            Engine.assume(columnTypes[i].isInstance(row[i]));
        }
    }

    @Override
    public Object[] first() {
        if (size != 0) {
            Object[] row = collectRow(0);
            ensureRow(row);
            return row;
        }

        return null;
    }

    @Override
    public void deleteAll() {
        size = 0;
        data = new Object[columnCount][];
        for (int i = 0; i < columnCount; i++) {
            data[i] = Engine.makeSymbolicArray(columnTypes[i], 0);
        }
    }
}
