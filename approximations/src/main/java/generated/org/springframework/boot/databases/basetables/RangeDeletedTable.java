package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.RangeDeletedTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

// TODO [REWRITE]:
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
    public Iterator<Object[]> iterator() {
        return new RangeDeletedTableIterator<>(this);
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
    public Optional<Object[]> findById(V id) {

        if (savedRowsStatus.containsKey(id)) {
            return savedRowsStatus.get(id) ? Optional.of(savedRows.get(id)) : Optional.empty();
        }

        Optional<Object[]> row = table.findById(id);
        if (row.isPresent() && !predicate.apply(row.get())) return row;

        return Optional.empty();
    }

}
