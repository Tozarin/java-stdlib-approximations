package generated.org.springframework.boot.databases.basetables;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class NoIdTableManager extends ANoIdTable implements ITableManager {

    public ANoIdTable tablesChain;

    public NoIdTableManager(Class<?>... columnTypes) {
        this.tablesChain = new NoIdTable(columnTypes);
    }

    @Override
    public void applyRangeUpdate(Function<Object[], Boolean> predicate, Function<Object[], Object[]> update) {
        tablesChain = new RangeUpdatedNoIdTable(tablesChain, predicate, update);
    }

    @Override
    public void applyRangeDelete(Function<Object[], Boolean> predicate) {
        tablesChain = new RangeDeletedNoIdTable(tablesChain, predicate);
    }

    @Override
    public int size() {
        return tablesChain.size();
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return tablesChain.iterator();
    }

    @Override
    public Class<Object[]> type() {
        return tablesChain.type();
    }

    @Override
    public int columnCount() {
        return tablesChain.columnCount();
    }

    @Override
    public Class<?>[] columnTypes() {
        return tablesChain.columnTypes();
    }

    @Override
    public void save(Object[] row) {
        tablesChain = new NoIdTableSave(tablesChain, row);
    }

    @Override
    public void delete(Object[] row) {
        tablesChain = new NoIdTableDelete(tablesChain, row);
    }

    @Override
    public void deleteAll() {
        tablesChain.deleteAll();
    }
}
