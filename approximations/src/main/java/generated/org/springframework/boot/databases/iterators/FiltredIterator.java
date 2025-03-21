package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.FiltredTable;
import generated.org.springframework.boot.databases.ITable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FiltredIterator<T> implements Iterator<T> {

    FiltredTable<T> filtredTable;
    Iterator<T> tblIter;
    T curr;

    public FiltredIterator(FiltredTable<T> filtredTable, boolean reversed) {
        this.filtredTable = filtredTable;
        ITable<T> table = filtredTable.table;
        this.tblIter = reversed ? table.backIterator() : table.iterator();
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
        if (!hasNext()) throw new NoSuchElementException();

        T tmp = curr;
        curr = null;

        return tmp;
    }
}
