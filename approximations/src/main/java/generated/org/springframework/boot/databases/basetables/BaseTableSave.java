package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableSaveIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaseTableSave<V> extends AChainedBaseTable<V> {

    public Object[] saved;

    public BaseTableSave(ABaseTable<V> table, Object[] saved) {
        this.table = table;
        this.saved = saved;
    }

    @Override
    public int size() {
        int count = 0;
        Iterator<Object[]> iter = iterator();
        while (iter.hasNext()) {
            Object[] ignored = iter.next();
            count++;
        }
        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableSaveIterator<>(this);
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        this.saved = null;
    }
}
