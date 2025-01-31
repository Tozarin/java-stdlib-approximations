package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.HashMap;
import java.util.Map;

public class DistinctTable<T> implements ITable<T> {

    public ITable<T> table;
    public int sizeOfTbl;
    public int size;

    public Map<T, Integer> cache;
    public int cacheSize;

    public DistinctTable(ITable<T> table) {

        this.table = table;
        this.sizeOfTbl = table.size();

        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);
        Engine.assume(size <= sizeOfTbl);

        this.cache = new HashMap<>();
        this.cacheSize = 0;
    }

    public DistinctTable(ITable<T> table, int sizeOfTbl, int size, Map<T, Integer> cache, int cacheSize) {
        this.table = table;
        this.sizeOfTbl = sizeOfTbl;
        this.size = size;
        this.cache = cache;
        this.cacheSize = cacheSize;
    }

    @Override
    public int size() {
        // Because it is aggregate operation
        // and we want to be sure that original table
        // contains size "good" rows and table.size() - size "bad" ones
        // Just ensure mapping
        for (int i = 0; i < size; i++) { getEnsure(i); }

        return size;
    }

    @Override
    public T getEnsure(int ix) {

        Engine.assume(ix < size);

        T t = table.getEnsure(ix);
        if (cacheSize == size) {
            Engine.assume(cache.containsKey(t));
            Engine.assume(cache.get(t) == ix);
            return t;
        }

        while (true) {

            if (!cache.containsKey(t)) {
                cache.put(t, ix);
                cacheSize++;
                return t;
            }

            if (cache.get(t) == ix) { return t; }

            ix++;
            Engine.assume(ix < sizeOfTbl);
        }
    }

    @Override
    public int indexIn(T t, int startIx, int endIx) {
        int ix = cache.get(t);
        if (ix < startIx || endIx <= ix) return -1;
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
        return new DistinctTable<>(
                table.clone(),
                size,
                sizeOfTbl,
                cache,
                cacheSize
        );
    }
}
