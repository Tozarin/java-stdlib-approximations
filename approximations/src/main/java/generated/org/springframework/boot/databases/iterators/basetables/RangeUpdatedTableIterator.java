package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.RangeUpdatedTable;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.Iterator;

public class RangeUpdatedTableIterator<V> implements Iterator<Object[]> {

    RangeUpdatedTable<V> table;
    public Iterator<Object[]> tableIter;

    int currSavedIx;
    public SymbolicMap<V, Object[]> savedRowsCopy;
    public SymbolicMap<V, Boolean> savedRowsStatusCopy;
    public SymbolicMap<Integer, V> saveOrderCopy;

    public Object[] curr;
    public boolean isEnded;

    public RangeUpdatedTableIterator(RangeUpdatedTable<V> table) {
        this.table = table;
        this.tableIter = table.iterator();

        this.currSavedIx = 0;
        this.savedRowsCopy = Engine.makeSymbolicMap();
        savedRowsCopy.merge(table.savedRows);
        this.savedRowsStatusCopy = Engine.makeSymbolicMap();
        savedRowsStatusCopy.merge(table.savedRowsStatus);
        this.saveOrderCopy = Engine.makeSymbolicMap();
        saveOrderCopy.merge(table.saveOrder);

        this.curr = null;
        this.isEnded = false;
    }

    public int nextSavedIx() { return currSavedIx++; }
    public boolean hasNextSaved() { return currSavedIx <= table.currSaveIx; }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasNext() {

        if (isEnded) return false;
        if (curr != null) return true;

        if (tableIter != null) {
            if (tableIter.hasNext()) {
                Object[] next = tableIter.next();
                curr = table.predicate.apply(next) ? table.update.apply(next) : next;

                V id = (V) curr[table.idColumnIx()];
                if (savedRowsCopy.containsKey(id)) {

                    if (savedRowsStatusCopy.get(id)) {
                        savedRowsStatusCopy.set(id, false);
                    }
                    else {
                        curr = null;
                        return hasNext();
                    }
                }

                return true;
            }

            tableIter = null;
        }

        if (hasNextSaved()) {
            V id = saveOrderCopy.get(nextSavedIx());

            if (!savedRowsStatusCopy.get(id)) {
                curr = null;
                return hasNext();
            }

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
