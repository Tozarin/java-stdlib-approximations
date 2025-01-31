package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.HashMap;
import java.util.Map;

public class DistinctTable<T> implements ITable<T> {

    public ITable<T> table;

    public Map<T, Integer> cache;
    public int cacheSize;

    public DistinctTable(ITable<T> table) {

        this.table = table;

        this.cache = new HashMap<>();
        this.cacheSize = 0;
    }

    public DistinctTable(ITable<T> table, Map<T, Integer> cache, int cacheSize) {
        this.table = table;
        this.cache = cache;
        this.cacheSize = cacheSize;
    }

    @Override
    public int size() {
        for (int i = 0; i < table.size(); i++) { getEnsure(i); }

        return cacheSize;
    }

    @Override
    public T getEnsure(int ix) {

        T t = table.getEnsure(ix);

        while (true) {
            if (!cache.containsKey(t)) {
                cache.put(t, ix);
                cacheSize++;
                return t;
            }

            if (cache.get(t) == ix) { return t; }

            t = table.getEnsure(ix++);
        }
    }

    @Override
    public int indexIn(T t, int startIx, int endIx) {
        if (cache.containsKey(t)) {
            int ix = cache.get(t);
            if (ix < startIx || endIx <= ix) return -1;
            return ix;
        }

        int ix = table.indexIn(t, startIx, endIx);
        if (ix != -1) cache.put(t, ix);
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
                cache,
                cacheSize
        );
    }
}
