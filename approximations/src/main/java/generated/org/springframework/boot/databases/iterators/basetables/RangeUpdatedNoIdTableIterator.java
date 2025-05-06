package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.RangeUpdatedNoIdTable;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;

public class RangeUpdatedNoIdTableIterator implements Iterator<Object[]> {

    public RangeUpdatedNoIdTable table;
    public Iterator<Object[]> tableIter;
    public SymbolicMap<Object[], Boolean> savedRowsCopy;

    public Object[] curr;
    public boolean isEnded;

    public RangeUpdatedNoIdTableIterator(RangeUpdatedNoIdTable table) {
        this.table = table;
        this.tableIter = table.iterator();
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
            if (tableIter.hasNext()) {
                Object[] next = tableIter.next();
                curr = table.predicate.apply(next) ? table.update.apply(next) : next;

                if (savedRowsCopy.containsKey(curr)) {

                    if (savedRowsCopy.get(curr)) {
                        savedRowsCopy.set(curr, false);
                    } else {
                        curr = null;
                        return hasNext();
                    }
                }

                return true;
            }

            tableIter = null;
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
