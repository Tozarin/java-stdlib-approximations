package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.MappedTable;

import java.util.Iterator;

public class MappedIterator<T, R> implements Iterator<R> {

    MappedTable<T, R> mappedTable;
    Iterator<T> tblIter;

    public MappedIterator(MappedTable<T, R> mappedTable) {
        this.mappedTable = mappedTable;
        this.tblIter = mappedTable.table.iterator();
    }

    @Override
    public boolean hasNext() {
        return tblIter.hasNext();
    }

    @Override
    public R next() {
        return mappedTable.applyMapper(tblIter.next());
    }
}
