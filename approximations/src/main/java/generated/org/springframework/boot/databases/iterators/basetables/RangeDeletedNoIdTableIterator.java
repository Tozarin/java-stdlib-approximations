package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.RangeDeletedNoIdTable;
import generated.org.springframework.boot.databases.iterators.utils.IteratorWithFilter;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;

public class RangeDeletedNoIdTableIterator implements Iterator<Object[]> {

    public RangeDeletedNoIdTable table;
    public Iterator<Object[]> tableIter;
    public SymbolicMap<Object[], Boolean> savedRowsCopy;

    public Object[] curr;
    public boolean isEnded;

    public RangeDeletedNoIdTableIterator(RangeDeletedNoIdTable table) {
        this.table = table;
        this.tableIter = new IteratorWithFilter<>(table.table.iterator(), table.predicate);
        this.savedRowsCopy = Engine.makeSymbolicMap();
        savedRowsCopy.merge(table.savedRows);

        this.curr = null;
        this.isEnded = false;
    }

    @Override
    public boolean hasNext() {

        if (isEnded) return false;
        if (curr != null) return true;

        if (tableIter != null) {
            if (!tableIter.hasNext()) {
                tableIter = null;
                return hasNext();
            }

            curr = tableIter.next();

            if (!savedRowsCopy.containsKey(curr) && !table.predicate.apply(curr)) return true;

            if (savedRowsCopy.get(curr)) {
                savedRowsCopy.set(curr, false);
                return true;
            }

            curr = null;
            return hasNext();
        }

        curr = savedRowsCopy.anyKey();
        if (savedRowsCopy.containsKey(curr)) {

            if (!savedRowsCopy.get(curr)) {
                savedRowsCopy.remove(curr);
                curr = null;
                return hasNext();
            }

            savedRowsCopy.remove(curr);
            return true;
        }

        isEnded = true;
        return false;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] tmp = curr;
        curr = null;
        return tmp;
    }
}
