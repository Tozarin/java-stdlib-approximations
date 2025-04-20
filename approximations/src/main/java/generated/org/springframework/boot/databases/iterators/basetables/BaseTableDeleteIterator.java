package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTableDelete;
import org.usvm.api.Engine;

import java.util.Iterator;

public class BaseTableDeleteIterator<V> implements Iterator<Object[]> {

    public BaseTableDelete<V> table;
    public Iterator<Object[]> tblIter;
    public Object[] removed;

    public Object[] curr;

    public BaseTableDeleteIterator(BaseTableDelete<V> table) {
        this.table = table;
        this.tblIter = table.table.iterator();
        this.removed = table.removed;
        this.curr = null;
    }

    @Override
    public boolean hasNext() {
        if (curr != null) return true;
        if (!tblIter.hasNext()) return false;

        curr = tblIter.next();
        if (removed != null && curr[table.idColumnIx()].equals(removed[table.idColumnIx()])) {
            removed = null;
            curr = null;
            return hasNext();
        }

        return true;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] tmp = curr;
        curr = null;

        return tmp;
    }
}
