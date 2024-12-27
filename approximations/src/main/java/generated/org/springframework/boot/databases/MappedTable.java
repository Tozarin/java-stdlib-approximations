package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

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

    @Override
    public R getEnsure(int ix) {

        T t = table.getEnsure(ix);

        return mapper.apply(t);
    }

    @Override
    public int indexIn(R r, int startIx, int endIx) {

        T t = Engine.makeSymbolic(table.type());
        Engine.assume(t != null);

        R mappedT = mapper.apply(t);
        Engine.assume(r == mappedT);

        return table.indexIn(t, startIx, endIx);
    }

    @Override
    public boolean containsIn(R r, int startIx, int endIx) {
        return indexIn(r, startIx, endIx) != -1;
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
