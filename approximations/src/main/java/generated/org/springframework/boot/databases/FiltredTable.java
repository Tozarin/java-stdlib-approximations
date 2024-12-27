package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.function.Predicate;

public class FiltredTable<T> implements ITable<T> {

    public ITable<T> table;
    public int size;
    public int[] indexMap;

    Predicate<T> filter;

    public FiltredTable(ITable<T> table, Predicate<T> filter) {

        this.table = table;
        this.size = Engine.makeSymbolicInt();
        Engine.assume(-1 < size);
        Engine.assume(size <= table.size());

        this.indexMap = Engine.makeSymbolicIntArray(size);
        Engine.assume(indexMap != null);

        this.filter = filter;
    }

    public FiltredTable(
            ITable<T> table,
            int size,
            int[] indexMap,
            Predicate<T> filter
    ) {
        this.table = table;
        this.size = size;
        this.indexMap = indexMap;
        this.filter = filter;
    }

    public int mapIndexEnsure(int ix) {

        int mappedIx = indexMap[ix];
        Engine.assume(-1 < mappedIx);
        Engine.assume(mappedIx < table.size());

        if (mappedIx == 0) Engine.assume(ix == 0);
        if (mappedIx == size - 1) Engine.assume(ix == size - 1);

        if (ix != 0) Engine.assume(indexMap[ix - 1] < mappedIx);
        if (ix != table.size() - 1) Engine.assume(mappedIx < indexMap[ix + 1]);

        return mappedIx;
    }


    // Because it is aggregate operation,
    // and we want to be sure that original table
    // contains size 'good' rows and table.size() - size 'bad' ones
    @Override
    public int size() {

        int ix = 0;
        for (int i = 0; i < table.size(); i++) {

            T t = table.getEnsure(i);

            if (ix == size || indexMap[ix] != i) {
                Engine.assume(!filter.test(t));
            }
            else {
                ix++;
                Engine.assume(filter.test(t));
            }
        }

        return size;
    }

    @Override
    public T getEnsure(int ix) {

        int origIx = mapIndexEnsure(ix);
        T t = table.getEnsure(origIx);
        Engine.assume(filter.test(t));

        return t;
    }

    @Override
    public int indexIn(T t, int startIx, int endIx) {

        Engine.assume(filter.test(t));

        int origStrIx = mapIndexEnsure(startIx);
        int origEndIx = mapIndexEnsure(endIx);
        int origIx = table.indexIn(t, origStrIx, origEndIx);

        if (origIx == -1) return -1;

        int ix = Engine.makeSymbolicInt();
        Engine.assume(startIx <= ix);
        Engine.assume(ix < endIx);
        Engine.assume(mapIndexEnsure(ix) == origIx);

        return ix;
    }

    @Override
    public boolean containsIn(T t, int startIx, int endIx) {
        return indexIn(t, startIx, endIx) != -1;
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public ITable<T> clone() {
        return new FiltredTable<>(
                table.clone(),
                size,
                indexMap,
                filter
        );
    }
}
