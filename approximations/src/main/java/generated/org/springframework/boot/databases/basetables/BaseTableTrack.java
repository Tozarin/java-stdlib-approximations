package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableTrackIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

// T - type of data-class
public class BaseTableTrack <T, V> extends AChainedBaseTable<V> {

    public Function<Object[], T> deserializer; // set later by generated code
    public String tableName;

    public BaseTableTrack(ABaseTable<V> table, String tableName) {
        this.table = table;
        this.tableName = tableName;
    }

    public void setDeserializer(Function<Object[], T> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
    }

    @Override
    public int size() {
        Iterator<Object[]> iter = iterator();
        int count = 0;
        while (iter.hasNext()) {
            Object[] ignored = iter.next();
            count++;
        }

        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableTrackIterator<>(this);
    }
}
