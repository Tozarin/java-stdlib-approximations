package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.NoIdTableSave;
import org.usvm.api.Engine;

import java.util.Iterator;

public class NoIdTableSaveIterator implements Iterator<Object[]> {

    public NoIdTableSave table;
    public Iterator<Object[]> tblIter;
    public Object[] saved;

    public NoIdTableSaveIterator(NoIdTableSave table) {
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

        Object[] row = saved;

        if (tblIter.hasNext()) {
            row = tblIter.next();

            if (saved != null && table.rowEquals(saved, row)) {
                row = saved;
                saved = null;
            }
        }

        return row;
    }
}
