package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTableSave;
import org.usvm.api.Engine;

import java.util.Iterator;

public class BaseTableSaveIterator<V> implements Iterator<Object[]> {

    public BaseTableSave<V> table;
    public Iterator<Object[]> tblIter;
    public Object[] saved;

    public BaseTableSaveIterator(BaseTableSave<V> table) {
        this.table = table;
        this.tblIter = table.table.iterator();
        this.saved = table.saved;
    }

    @Override
    public boolean hasNext() {
        if (tblIter.hasNext()) return true;
        return saved != null;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] row = null;
        if (!tblIter.hasNext()) {
            Engine.assume(saved != null);
            row = saved;
            saved = null;
        } else {
            row = tblIter.next();
            if (saved != null && row[table.idColumnIx()].equals(saved[table.idColumnIx()])) {
                row = saved;
                saved = null;
            }
        }

        return row;
    }
}
