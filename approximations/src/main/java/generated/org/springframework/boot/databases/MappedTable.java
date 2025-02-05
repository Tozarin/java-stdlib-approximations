package generated.org.springframework.boot.databases;

import java.util.Iterator;
import java.util.function.Function;

public class MappedTable<T, R> implements ITable<R> {

    public ITable<T> table;
    public int size;

    public Class<R> type;
    public Function<T, R> mapper;

    public MappedTable(ITable<T> table, Function<T, R> mapper, Class<R> type) {

        this.table = table;
        this.size = table.size();
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    public int size() {
        return size;
    }

    class MappedIterator implements Iterator<R> {

        Iterator<T> tblIter;

        public MappedIterator() {
            this(false);
        }

        public MappedIterator(boolean reversed) {
            if (reversed) this.tblIter = table.clone().backIterator();
            this.tblIter = table.clone().iterator();
        }

        @Override
        public boolean hasNext() {
            return tblIter.hasNext();
        }

        @Override
        public R next() {
            return mapper.apply(tblIter.next());
        }
    }

    @Override
    public Iterator<R> iterator() {
        return new MappedIterator();
    }

    @Override
    public Iterator<R> backIterator() {
        return new MappedIterator(true);
    }

    @Override
    public Class<R> type() {
        return type;
    }

    @Override
    public ITable<R> clone() {
        return new MappedTable<>(
                table.clone(),
                mapper,
                type
        );
    }
}
