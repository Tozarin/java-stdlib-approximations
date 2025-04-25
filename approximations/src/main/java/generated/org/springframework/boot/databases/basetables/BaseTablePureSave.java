package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTablePureIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaseTablePureSave<V> extends AChainedBaseTable<V> {

    public Object[] saved;

    public BaseTablePureSave(ABaseTable<V> table, Object[] saved) {
        this.table = table;
        this.saved = saved;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        saved = null;
    }

    @Override
    public int size() { return table.size() + 1; }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return new BaseTablePureIterator<>(this); }
}
