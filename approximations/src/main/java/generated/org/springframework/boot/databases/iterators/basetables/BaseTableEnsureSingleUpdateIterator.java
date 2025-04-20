package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTableEnsureSingleUpdate;
import org.usvm.api.Engine;

import java.util.Iterator;

public class BaseTableEnsureSingleUpdateIterator<V> implements Iterator<Object[]> {

    public BaseTableEnsureSingleUpdate<V> table;
    public Iterator<Object[]> tblIter;

    public V id;
    public int pos;
    public Object value;

    public boolean updated;

    public BaseTableEnsureSingleUpdateIterator(BaseTableEnsureSingleUpdate<V> table) {
        this.table = table;
        this.tblIter = table.table.iterator();

        this.id = table.id;
        this.pos = table.pos;
        this.value = table.value;

        this.updated = false;
    }


    @Override
    public boolean hasNext() {
        if (!tblIter.hasNext()) {
            Engine.assume(value == null);
            return false;
        }

        return true;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] row = tblIter.next();

        if (value != null && row[table.idColumnIx()].equals(id)) {
            row[pos] = value;
            value = null;
            return row;
        }

        return row;
    }
}
