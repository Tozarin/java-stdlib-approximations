package generated.org.springframework.boot.databases.iterators.utils;

import generated.org.springframework.boot.databases.basetables.ABaseTable;
import org.usvm.api.Engine;

import java.util.Iterator;

public class FindAllByIdIterator<V> implements Iterator<Object[]> {

    ABaseTable<V> table;
    Iterator<V> keysIter;
    V currKey;

    public FindAllByIdIterator(ABaseTable<V> table, Iterable<V> keys) {
        this.table = table;
        this.keysIter = keys.iterator();
        this.currKey = null;
    }

    @Override
    public boolean hasNext() {
        if (currKey != null) return true;

        while (keysIter.hasNext()) {
            V key = keysIter.next();
            if (table.existsById(key)) {
                currKey = key;
                return true;
            }
        }

        return false;
    }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] row = table.findById(currKey).get();
        currKey = null;

        return row;
    }
}
