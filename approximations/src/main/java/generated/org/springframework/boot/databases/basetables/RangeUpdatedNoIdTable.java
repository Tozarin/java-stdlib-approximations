package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.RangeUpdatedNoIdTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;
import org.usvm.api.internal.SymbolicMapImpl;

import java.util.Iterator;
import java.util.function.Function;

// TODO [REWRITE]:
public class RangeUpdatedNoIdTable extends AChainedNoIdTable {

    // key - row
    // value - flag of contains:
    //  - true if save was called with value as argument
    //  - false if delete was called with value as argument
    public SymbolicMap<Object[], Boolean> savedRows;

    public Function<Object[], Boolean> predicate;
    public Function<Object[], Object[]> update;

    public RangeUpdatedNoIdTable(
            ANoIdTable table,
            Function<Object[], Boolean> predicate,
            Function<Object[], Object[]> update
    ) {
        this.table = table;
        this.predicate = predicate;
        this.update = update;
        this.savedRows = Engine.makeSymbolicMap();
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
        return new RangeUpdatedNoIdTableIterator(this);
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
        savedRows = new SymbolicMapImpl<>();
    }
}
