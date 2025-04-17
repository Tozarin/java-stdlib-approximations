package generated.org.springframework.boot.databases.basetables;

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
        return 0;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return null;
    }

    @Override
    public Iterator<Object[]> backIterator() {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
