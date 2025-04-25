package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTablePureSave;
import org.usvm.api.Engine;

import java.util.Iterator;

public class BaseTablePureIterator<V> implements Iterator<Object[]> {

    public BaseTablePureSave<V> table;
    public Iterator<Object[]> tblIter;
    public Object[] saved;

    public BaseTablePureIterator(BaseTablePureSave<V> table) {
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

        if (tblIter.hasNext()) {
            Object[] row = tblIter.next();
            Engine.assume(row[table.idColumnIx()].equals(saved[table.idColumnIx()]));
            return row;
        }

        Object[] tmp = saved;
        saved = null;

        return tmp;
    }
}
