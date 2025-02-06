package generated.org.springframework.boot.databases;

import kotlin.jvm.functions.Function2;

import java.util.Iterator;

public class MappedTable<T, R> implements ITable<R> {

    public ITable<T> table;
    public int size;

    public Class<R> type;
    public Function2<T, Object[], R> mapper;

    // arguments of original repository method
    Object[] methodArgs;

    public MappedTable(ITable<T> table, Function2<T, Object[], R> mapper, Class<R> type, Object[] methodArgs) {

        this.table = table;
        this.size = table.size();
        this.mapper = mapper;
        this.type = type;
        this.methodArgs = methodArgs;
    }

    public R applyMapper(T t) {
        return mapper.invoke(t, methodArgs);
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
            return applyMapper(tblIter.next());
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
                type,
                methodArgs
        );
    }
}
