package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.BaseTable;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BaseTableIterator<V> implements Iterator<Object[]> {

    BaseTable<V> table;

    int ix;
    int endIx;

    boolean reversed;
    Object[] curr;

    public BaseTableIterator(BaseTable<V> table) {
        this(table, false);
    }

    public BaseTableIterator(BaseTable<V> table, boolean reversed) {
        this.table = table;

        this.ix = reversed ? table.size() - 1 : 0;
        this.endIx = table.size();

        this.reversed = reversed;
        this.curr = null;
    }

    private int nextIndex() {
        return reversed ? ix-- : ix++;
    }

    private boolean condition() {
        return reversed ? ix < 0 : endIx <= ix;
    }

    @Override
    public boolean hasNext() {

        if (curr != null) return true;

        while (table.removedIx.contains(ix)) {
            nextIndex();
        }

        if (!condition()) {
            curr = table.getEnsure(nextIndex());
            return true;
        }

        return false;
    }

    @Override
    public Object[] next() {
        if (!hasNext()) throw new NoSuchElementException();

        Object[] tmp = curr;
        curr = null;

        return tmp;
    }
}
