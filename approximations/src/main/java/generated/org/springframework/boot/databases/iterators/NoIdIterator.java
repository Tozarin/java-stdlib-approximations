package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.NoIdTable;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NoIdIterator implements Iterator<Object[]> {

    NoIdTable table;

    int ix;
    int endIx;

    boolean reversed;

    public NoIdIterator(NoIdTable table) {
        this(table, false);
    }

    public NoIdIterator(NoIdTable table, boolean reversed) {
        this.table = table;
        this.ix = reversed ? table.size - 1 : 0;
        this.endIx = table.size;
        this.reversed = reversed;
    }

    @Override
    public boolean hasNext() {
        return reversed ? 0 <= ix : ix < endIx;
    }

    @Override
    public Object[] next() {
        if (!hasNext()) throw new NoSuchElementException();
        return table.getRow(reversed ? ix-- : ix++);
    }
}
