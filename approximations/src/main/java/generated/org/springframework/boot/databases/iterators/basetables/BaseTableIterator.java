package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTable;
import org.usvm.api.Engine;
import org.usvm.api.internal.SymbolicListImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseTableIterator<V> implements Iterator<Object[]> {

    BaseTable<V> table;

    int ix;
    int endIx;

    boolean reversed;
    Object[] curr;

    ArrayList<V> returnedIds;

    public BaseTableIterator(BaseTable<V> table) {
        this(table, false);
    }

    @SuppressWarnings("unchecked")
    public BaseTableIterator(BaseTable<V> table, boolean reversed) {
        this.table = table;

        this.ix = reversed ? table.size() - 1 : 0;
        this.endIx = table.size();

        this.reversed = reversed;
        this.curr = null;

        this.returnedIds = new ArrayList<>();
    }

    private int nextIndex() {
        return reversed ? ix-- : ix++;
    }

    private boolean condition() {
        return reversed ? ix < 0 : endIx <= ix;
    }

    private void ensureId(V id) {

        for (int i = 0; i < returnedIds.size(); i++) Engine.assume(!returnedIds.get(i).equals(id));
        returnedIds.add(id);
    }

    @Override
    public boolean hasNext() { return !condition(); }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] next() {
        Engine.assume(hasNext());

        int newIx = nextIndex();
        Object[] row = table.getRowEnsure(newIx);

        ensureId((V) row[table.idIndex]);

        return row;
    }
}
