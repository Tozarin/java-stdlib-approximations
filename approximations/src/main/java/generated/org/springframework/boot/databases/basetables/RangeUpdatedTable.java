package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.RangeUpdatedTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;
import org.usvm.api.internal.SymbolicMapImpl;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;


// TODO: rewrite without SymbolicMap
// V - type of id field
public class RangeUpdatedTable<V> extends AChainedBaseTable<V> {

    public int currSize;

    public SymbolicMap<V, Object[]> savedRows; // value is row
    public SymbolicMap<V, Boolean> savedRowsStatus; // value is status of row: true - was saved, false - was deleted

    public SymbolicMap<Integer, V> saveOrder; // necessary for backIterator
    public int currSaveIx;

    public Function<Object[], Boolean> predicate;
    public Function<Object[], Object[]> update;

    public RangeUpdatedTable(
            ABaseTable<V> table,
            Function<Object[], Boolean> predicate,
            Function<Object[], Object[]> update
    ) {
        this.table = table;
        this.currSize = table.size();
        this.predicate = predicate;
        this.update = update;

        this.savedRows = new SymbolicMapImpl<>();
        this.savedRowsStatus = new SymbolicMapImpl<>();

        this.saveOrder = new SymbolicMapImpl<>();
        this.currSaveIx = 0;
    }

    @Override
    public int size() { return currSize; }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return new RangeUpdatedTableIterator<>(this); }

    @Override
    public void deleteAll() {
        table.deleteAll();
        currSize = 0;
        savedRows = Engine.makeSymbolicMap();
        savedRowsStatus = Engine.makeSymbolicMap();
        saveOrder = Engine.makeSymbolicMap();
        currSaveIx = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Object[]> findById(V id) {

        if (savedRowsStatus.containsKey(id)) {
            return savedRowsStatus.get(id) ? Optional.of(savedRows.get(id)) : Optional.empty();
        }

        // we could have changed the id, so we must check it
        // so we take random id possibly in subtable and do check
        V oldId = (V) Engine.makeSymbolic(columnTypes()[idColumnIx()]);
        Engine.assume(oldId != null);
        Optional<Object[]> row = table.findById(oldId);

        if (row.isPresent()) {
            Object[] unpack = row.get();
            if (predicate.apply(unpack)) return Optional.of(update.apply(unpack));
        }

        return Optional.empty();
    }
}
