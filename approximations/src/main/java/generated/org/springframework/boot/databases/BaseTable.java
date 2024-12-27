package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

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

    @Override
    public Object[] getEnsure(int ix) {

        Engine.assume(ix < size());
        Object[] row = getRow(ix);
        V id = (V) row[idIndex];
        Engine.assume(ids.containsKey(id));
        Engine.assume(ids.get(id) == ix);

        return row;
    }

    @Override
    public int indexIn(Object[] row, int startIx, int endIx) {

        V id = (V) row[idIndex];
        if (ids.containsKey(id)) {
            int ix = ids.get(id);
            Engine.assume(ix < size());
            Engine.assume(data[idIndex][ix] == id);
            if (startIx <= ix && ix < endIx) return ix;
        }

        return -1;
    }

    @Override
    public boolean containsIn(Object[] row, int startIx, int endIx) {
        return indexIn(row, startIx, endIx) != -1;
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
