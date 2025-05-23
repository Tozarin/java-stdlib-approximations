package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.MappedTable;
import generated.org.springframework.boot.databases.wrappers.ListWrapper;
import jakarta.validation.ConstraintValidator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.function.Function;

public class BaseTableManager<T, V> extends ABaseTable<V> implements ITableManager {

    public BaseTableTrack<T, V> trackTable; // nullable
    public ABaseTable<V> tablesChain;

    public String tableName;
    public Class<T> entityType;

    public BaseTableManager(
            int idIndex,
            Class<T> entityType,
            Class<?>[] columnTypes,
            ConstraintValidator<?, ?>[][] validators,
            String tableName,
            boolean needTrack // true if SpringBootTests option set in WebBench
    ) {
        this.tableName = tableName;

        BaseTable<V> base = new BaseTable<>(idIndex, columnTypes);
        BaseTableConstraintValidate<V> validated = new BaseTableConstraintValidate<>(base, validators);

        if (needTrack) {
            BaseTableTrack<T, V> track = new BaseTableTrack<>(validated, tableName);
            this.trackTable = track;
            this.tablesChain = track;
        }
        else {
            this.tablesChain = validated;
        }

        this.entityType = entityType;
    }

    public void setDeserializerTrackTable(Function<Object[] , T> deserializer) {
        if (trackTable != null) trackTable.setDeserializer(deserializer);
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
        V symbId = symbolizeId(id);
        tablesChain = new BaseTableEnsureSingleUpdate<>(tablesChain, symbId, pos, v);
    }

    public void pureSave(Object[] row) {
        tablesChain = new BaseTablePureSave<>(tablesChain, row);
    }

    @SuppressWarnings("unchecked")
    public V symbolizeId(V id) {
        V symbId = (V) Engine.makeSymbolic(tablesChain.columnTypes()[tablesChain.idColumnIx()]);
        Engine.assume(id != null);
        Engine.assume(id.equals(symbId));
        return symbId;
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
    public int idColumnIx() {
        return tablesChain.idColumnIx();
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
    public void save(Object[] row) {
        tablesChain = new BaseTableSave<>(tablesChain, row);
    }

    @Override
    public void delete(Object[] row) {
        tablesChain = new BaseTableDelete<>(tablesChain, row);
    }

    @Override
    public void deleteAll() {
        tablesChain.deleteAll();
    }
}
