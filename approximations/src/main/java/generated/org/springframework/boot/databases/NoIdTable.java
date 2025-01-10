package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

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
    public Object[] getEnsure(int ix) {

        Engine.assume(ix < size());
        return getRow(ix);
    }

    public boolean rowEqual(Object[] l, Object[] r) {

        for (int i = 0; i < columnCount; i++) {
            if (l[i] != r[i] || !l[i].equals(r[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int indexIn(Object[] row, int startIx, int endIx) {

        for (int i = startIx; i < endIx; i++) {

            Object[] candidate = getRow(i);
            if (rowEqual(row, candidate)) {
                return i;
            }
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
        return new NoIdTable(
                data,
                size,
                columnCount
        );
    }
}
