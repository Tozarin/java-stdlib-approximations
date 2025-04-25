package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.NoIdTable;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;
import org.usvm.api.internal.SymbolicMapImpl;

import java.util.Iterator;

public class NoIdIterator implements Iterator<Object[]> {

    public NoIdTable table;
    public SymbolicMap<Object[], Object> data;
    public Object[] curr;

    public boolean isEnded;

    public NoIdIterator(NoIdTable table) {
        SymbolicMap<Object[], Object> newData = new SymbolicMapImpl<>();
        newData.merge(table.data);
        this.data = newData;

        this.table = table;
        this.curr = null;
        this.isEnded = false;
    }

    @Override
    public boolean hasNext() {

        if (isEnded) return false;
        if (curr != null) return true;

        curr = data.anyKey();
        // any key not contained means that map is empty
        if (data.containsKey(curr)) {

            table.ensureRow(curr);

            data.remove(curr);
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
