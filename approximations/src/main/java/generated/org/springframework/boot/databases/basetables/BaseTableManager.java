package generated.org.springframework.boot.databases.basetables;

import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class BaseTableManager<V> extends ABaseTable<V> implements ITableManager {

    public ABaseTable<V> tablesChain;

    public BaseTableManager(int idIndex, Class<?>... columnTypes) {
        this.tablesChain = new BaseTable<>(idIndex, columnTypes);
    }

    @Override
    public void applyRangeUpdate(Function<Object[], Boolean> predicate, Function<Object[], Object[]> update) {
        tablesChain = new RangeUpdatedTable<>(tablesChain, predicate, update);
    }

    @Override
    public void applyRangeDelete(Function<Object[], Boolean> predicate) {
        tablesChain = new RangeDeletedTable<>(tablesChain, predicate);
    }

    public void changeSingleFieldByIdEnsure(V id, int pos, Object v) {
        Optional<Object[]> rowOpt = findById(id);
        Engine.assume(rowOpt.isPresent());
        Object[] row = rowOpt.get();
        row[pos] = v;
        save(row);
    }

    @Override
    public int columnCount() { return tablesChain.columnCount(); }

    @Override
    public Class<?>[] columnTypes() { return tablesChain.columnTypes(); }

    @Override
    public int idColumnIx() { return tablesChain.idColumnIx(); }

    @Override
    public int size() { return tablesChain.size(); }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return tablesChain.iterator(); }

    @Override
    public Iterator<Object[]> backIterator() { return tablesChain.backIterator(); }

    @Override
    public Class<Object[]> type() { return tablesChain.type(); }

    @Override
    public void save(Object[] row) { tablesChain.save(row); }

    @Override
    public void delete(Object[] row) {}

    @Override
    public void deleteAll() { tablesChain.deleteAll(); }
}
