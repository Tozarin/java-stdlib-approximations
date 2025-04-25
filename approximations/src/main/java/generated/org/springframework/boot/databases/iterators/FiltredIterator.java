package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.FiltredTable;
import generated.org.springframework.boot.databases.ITable;
import org.usvm.api.Engine;

import java.util.Iterator;

public class FiltredIterator<T> implements Iterator<T> {

    public FiltredTable<T> filtredTable;
    public Iterator<T> tblIter;
    public T curr;

    public FiltredIterator(FiltredTable<T> filtredTable) {
        this.filtredTable = filtredTable;
        ITable<T> table = filtredTable.table;
        this.tblIter = table.iterator();
        this.curr = null;
    }

    @Override
    public boolean hasNext() {

        if (curr != null) return true;

        while (tblIter.hasNext()) {
            T candidate = tblIter.next();
            if (filtredTable.callFilter(candidate)) {
                curr = candidate;
                return true;
            }
        }

        return false;
    }

    @Override
    public T next() {
        Engine.assume(hasNext());

        T tmp = curr;
        curr = null;

        return tmp;
    }
}
