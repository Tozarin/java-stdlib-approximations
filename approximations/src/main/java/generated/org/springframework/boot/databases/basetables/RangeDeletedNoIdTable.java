package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.RangeDeletedNoIdTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;
import org.usvm.api.internal.SymbolicMapImpl;

import java.util.Iterator;
import java.util.function.Function;

// TODO [REWRITE]:
public class RangeDeletedNoIdTable extends AChainedNoIdTable {

    public SymbolicMap<Object[], Boolean> savedRows;

    public Function<Object[], Boolean> predicate;

    public RangeDeletedNoIdTable(ANoIdTable table, Function<Object[], Boolean> predicate) {
        this.table = table;
        this.predicate = predicate;
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
    public Iterator<Object[]> iterator() { return new RangeDeletedNoIdTableIterator(this); }

    @Override
    public void deleteAll() {
        table.deleteAll();
        savedRows = new SymbolicMapImpl<>();
    }
}
