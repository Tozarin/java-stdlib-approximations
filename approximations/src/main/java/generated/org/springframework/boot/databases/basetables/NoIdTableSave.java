package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.NoIdTableSaveIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class NoIdTableSave extends AChainedNoIdTable {

    public Object[] saved;

    public int cachedSize;

    public NoIdTableSave(ANoIdTable table, Object[] saved) {
        this.table = table;
        this.saved = saved;
        this.cachedSize = -1;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        saved = null;
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
    public Iterator<Object[]> iterator() {
        return new NoIdTableSaveIterator(this);
    }
}
