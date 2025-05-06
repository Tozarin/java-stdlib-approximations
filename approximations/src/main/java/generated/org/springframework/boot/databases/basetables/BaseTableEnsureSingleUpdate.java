package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableEnsureSingleUpdateIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaseTableEnsureSingleUpdate<V> extends AChainedBaseTable<V> {

    public V id;
    public int pos;
    public Object value;

    public BaseTableEnsureSingleUpdate(ABaseTable<V> table, V id, int pos, Object value) {
        this.table = table;
        this.id = id;
        this.pos = pos;
        this.value = value;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        value = null;
    }

    @Override
    public int size() {
        return table.size();
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableEnsureSingleUpdateIterator<>(this);
    }
}
