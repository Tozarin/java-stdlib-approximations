package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.NoIdTable;
import org.usvm.api.Engine;

import java.util.Iterator;

public class NoIdIterator implements Iterator<Object[]> {

    public NoIdTable table;

    public int ix;
    public int endIx;

    public NoIdIterator(NoIdTable table) {
        this.table = table;

        this.ix = 0;
        this.endIx = table.size();
    }

    @Override
    public boolean hasNext() {
        return ix < endIx;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] row = table.collectRow(ix++);
        return row;
    }
}
