package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableDeleteIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaseTableDelete<V> extends AChainedBaseTable<V> {

    public Object[] removed;

    public int cachedSize;

    public BaseTableDelete(ABaseTable<V> table, Object[] removed) {
        this.table = table;
        this.removed = removed;
        this.cachedSize = -1;
    }

    @Override
    public int size() {
        if (cachedSize != -1) return cachedSize;

        int count = 0;
        for (Object[] ignored : this) count++;
        cachedSize = count;
        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return new BaseTableDeleteIterator<>(this); }

    @Override
    public void deleteAll() {
        table.deleteAll();
        removed = null;
    }
}
