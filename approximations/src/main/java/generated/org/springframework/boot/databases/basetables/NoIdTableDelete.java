package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.NoIdTableDeleteIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class NoIdTableDelete extends AChainedNoIdTable {

    public Object[] deleted;
    public int cachedSize;

    public NoIdTableDelete(ANoIdTable table, Object[] deleted) {
        this.table = table;
        this.deleted = deleted;
        cachedSize = -1;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        deleted = null;
        cachedSize = 0;
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
    public Iterator<Object[]> iterator() { return new NoIdTableDeleteIterator(this); }
}
