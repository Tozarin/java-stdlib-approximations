package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;
import runtime.LibSLRuntime;

import java.util.function.Function;

public class JoinedTable<L, R> implements ITable<Object[]> {

    public ITable<L> leftTable;
    public ITable<R> rightTable;

    public int leftSize;
    public int rightSize;

    public Function<L, Object[]> leftSerializer;
    public Function<R, Object[]> rightSerializer;

    public JoinedTable(
            ITable<L> leftTable,
            ITable<R> rightTable,
            Function<L, Object[]> leftSerializer,
            Function<R, Object[]> rightSerializer
    ) {
        this.leftTable = leftTable;
        this.rightTable = rightTable;

        this.leftSerializer = leftSerializer;
        this.rightSerializer = rightSerializer;

        this.leftSize = leftTable.size();
        this.rightSize = rightTable.size();
    }

    public int leftIx(int ix) {

        if (rightSize == 0) return -1;

        return ix / rightSize;
    }

    public int rightIx(int ix) {

        if (leftSize == 0) return -1;

        return ix % leftSize;
    }

    public Object[] composite(L l, R r) {

        Object[] lRow = leftSerializer.apply(l);
        Object[] rRow = rightSerializer.apply(r);

        Object[] row = new Object[lRow.length + rRow.length];
        LibSLRuntime.ArrayActions.copy(lRow, 0, row, 0, lRow.length);
        LibSLRuntime.ArrayActions.copy(rRow, 0, row, lRow.length, rRow.length);

        return row;
    }

    @Override
    public int size() {
        return leftSize * rightSize;
    }

    @Override
    public Object[] getEnsure(int ix) {

        L l = leftTable.getEnsure(leftIx(ix));
        R r = rightTable.getEnsure(rightIx(ix));

        return composite(l, r);
    }

    @Override
    public int indexIn(Object[] row, int startIx, int endIx) {

        int lStrIx = leftIx(startIx);
        int lEndIx = leftIx(endIx);
        if (lStrIx == lEndIx) lEndIx++;

        int rStrIx = rightIx(startIx);
        int rEndIx = rightIx(endIx);
        if (rStrIx == rEndIx) rEndIx++;

        L l = Engine.makeSymbolic(leftTable.type());
        Engine.assume(l != null);
        R r = Engine.makeSymbolic(rightTable.type());
        Engine.assume(r != null);

        Object[] composedRow = composite(l, r);
        for (int i = 0; i < row.length; i++) Engine.assume(row[i] == composedRow[i]);

        int lIx = leftTable.indexIn(l, lStrIx, lEndIx);
        int rIx = rightTable.indexIn(r, rStrIx, rEndIx);

        if (lIx == -1 || rIx == -1) return -1;

        return leftSize * lIx + rIx;
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
        return new JoinedTable<>(
                leftTable.clone(),
                rightTable.clone(),
                leftSerializer,
                rightSerializer
        );
    }
}
