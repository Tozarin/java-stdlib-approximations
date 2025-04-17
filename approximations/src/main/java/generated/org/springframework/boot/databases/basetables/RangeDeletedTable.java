package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.utils.FindAllByIdIterator;
import generated.org.springframework.boot.databases.iterators.basetables.RangeDeletedTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;
import org.usvm.api.internal.SymbolicMapImpl;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class RangeDeletedTable<V> extends AChainedBaseTable<V> {

    public SymbolicMap<V, Object[]> savedRows; // value is row
    public SymbolicMap<V, Boolean> savedRowsStatus; // value is status of row: true - was saved, false - was deleted

    public SymbolicMap<Integer, V> saveOrder; // necessary for backIterator
    public int currSaveIx;

    public Function<Object[], Boolean> predicate;

    public RangeDeletedTable(ABaseTable<V> table, Function<Object[], Boolean> predicate) {
        this.table = table;
        this.predicate = predicate;

        this.savedRows = Engine.makeSymbolicMap();
        this.savedRowsStatus = Engine.makeSymbolicMap();

        this.saveOrder = Engine.makeSymbolicMap();
        this.currSaveIx = 0;
    }


    @Override
    public int size() {
        int count = 0;
        for (Object[] ignored : this) count++;
        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return new RangeDeletedTableIterator<>(this); }

    @Override
    public Iterator<Object[]> backIterator() { return new RangeDeletedTableIterator<>(this, true); }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Object[] row) {
        V id = (V) row[idColumnIx()];

        if (!existsById(id)) saveOrder.set(currSaveIx++, id);

        Object[] oldRow = savedRows.get(id);
        Object[] merged = mergeRows(oldRow, row);
        savedRows.set(id, merged);
        savedRowsStatus.set(id, true);
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        savedRows = Engine.makeSymbolicMap();
        savedRowsStatus = Engine.makeSymbolicMap();
        saveOrder = Engine.makeSymbolicMap();
        currSaveIx = 0;
    }

    @Override
    public void deleteById(V id) {
        Optional<Object[]> row = findById(id);
        if (row.isPresent()) {
            savedRows.set(id, row.get());
            savedRowsStatus.set(id, false);
        }
    }

    @Override
    public boolean existsById(V id) { return findById(id).isPresent(); }

    @Override
    public Optional<Object[]> findById(V id) {

        if (savedRowsStatus.containsKey(id)) {
            return savedRowsStatus.get(id) ? Optional.of(savedRows.get(id)) : Optional.empty();
        }

        Optional<Object[]> row = table.findById(id);
        if (row.isPresent() && !predicate.apply(row.get())) return row;

        return Optional.empty();
    }

    @Override
    public Iterable<Object[]> findAllById(Iterable<V> keys) {
        class FindAllByIdIterable implements Iterable<Object[]> {

            public RangeDeletedTable<V> table;

            public FindAllByIdIterable(RangeDeletedTable<V> table) { this.table = table; }

            @NotNull
            @Override
            public Iterator<Object[]> iterator() {
                return new FindAllByIdIterator<>(table, keys);
            }
        }

        return new FindAllByIdIterable(this);
    }
}
