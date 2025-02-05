package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;
import java.util.NoSuchElementException;

// V --- type of id field
public class BaseTable<V> implements ITable<Object[]> {

    //      row0  row2  --  rowN
    //  col0
    //  col1
    //   --
    //  colM
    public Object[][] data;
    public int size;

    public SymbolicMap<V, Integer> ids;

    public int columnCount;
    public int idIndex;

    public BaseTable(
            int idIndex,
            Class<?>... columnTypes) {

        this.columnCount = columnTypes.length;
        this.idIndex = idIndex;
        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);

        this.data = new Object[columnCount][];

        for (int i = 0; i < columnCount; i++) {
            data[i] = Engine.makeSymbolicArray(columnTypes[i], size);
            Engine.assume(data[i] != null);
        }

        this.ids = Engine.makeSymbolicMap();
        Engine.assume(ids != null);
        Engine.assume(ids.size() == size);
    }

    public BaseTable(
            Object[][] data,
            int size,
            SymbolicMap<V, Integer> ids,
            int columnCount,
            int idIndex
    ) {
        this.data = data;
        this.size = size;
        this.ids = ids;
        this.columnCount = columnCount;
        this.idIndex = idIndex;
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

    @SuppressWarnings("unchecked")
    public Object[] getEnsure(int ix) {

        Engine.assume(ix < size());
        Object[] row = getRow(ix);
        V id = (V) row[idIndex];
        Engine.assume(ids.containsKey(id));
        Engine.assume(ids.get(id) == ix);

        return row;
    }

    class BaseTableIterator implements Iterator<Object[]> {

        int ix;
        int endIx;

        public BaseTableIterator() {
            this.ix = 0;
            this.endIx = size();
        }

        @Override
        public boolean hasNext() {
            return ix < endIx;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            return getEnsure(ix++);
        }
    }

    class BaseTableBackIterator implements Iterator<Object[]> {

        int ix;

        public BaseTableBackIterator() {
            this.ix = size() - 1;
        }

        @Override
        public boolean hasNext() {
            return 0 <= ix;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            return getEnsure(ix--);
        }
    }

    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableIterator();
    }

    @Override
    public Iterator<Object[]> backIterator() {
        return new BaseTableBackIterator();
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    @Override
    public ITable<Object[]> clone() {
        return new BaseTable<>(
                data,
                size,
                ids,
                columnCount,
                idIndex
        );
    }
}
