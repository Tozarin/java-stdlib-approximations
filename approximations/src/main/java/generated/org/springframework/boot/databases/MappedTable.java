package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.MappedIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MappedTable<T, R> implements ITable<R> {

    public ITable<T> table;

    public Class<R> type;
    public Function<T, R> mapper;
    public BiFunction<T, Object[], R> mapper2;

    // arguments of original repository method
    Object[] methodArgs;

    public MappedTable(ITable<T> table, BiFunction<T, Object[], R> mapper, Class<R> type, Object[] methodArgs) {
        this.table = table;
        this.mapper2 = mapper;
        this.type = type;
        this.methodArgs = methodArgs;
    }

    public MappedTable(ITable<T> table, Function<T, R> mapper, Class<R> type) {
        this.table = table;
        this.mapper = mapper;
        this.type = type;
    }

    public R applyMapper(T t) {
        return mapper != null ? mapper.apply(t) : mapper2.apply(t, methodArgs);
    }

    @Override
    public int size() {
        return table.size();
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new MappedIterator<>(this);
    }

    @Override
    public Class<R> type() {
        return type;
    }

    @Override
    public R first() {
        T t = table.first();
        if (t != null) return applyMapper(t);
        return null;
    }
}
