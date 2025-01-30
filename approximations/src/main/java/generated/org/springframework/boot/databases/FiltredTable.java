package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class FiltredTable<T> implements ITable<T> {

    public ITable<T> table;
    public int sizeOfTbl;
    public int size;

    // subtableIx -> thisIx
    // to prevent situations like this:
    // this.get(3) -> return table.get(5) because 3, 4 are bad
    // this.get(5) will return table.get(5), but 5 != 3 !!
    //
    // to counter this here we note that 3 -> 5 and will use it in check
    public Integer[] mapper;

    Predicate<T> filter;

    public FiltredTable(ITable<T> table, Predicate<T> filter) {
        this.table = table;
        this.sizeOfTbl = table.size();
        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);
        Engine.assume(size <= sizeOfTbl);

        this.mapper = new Integer[sizeOfTbl];

        this.filter = filter;
    }

    public FiltredTable(
            ITable<T> table,
            int sizeOfTbl,
            int size,
            Integer[] mapper,
            Predicate<T> filter
    ) {
        this.table = table;
        this.sizeOfTbl = sizeOfTbl;
        this.size = size;
        this.mapper = mapper;
        this.filter = filter;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T getEnsure(int ix) {

        Engine.assume(ix < size);

        // ix < mapper[i] forall i >= ix // invariant
        // see info about other indexes
        // mapper[i] == null means hz about table.getEnsure(i)
        int i = ix;
        while(mapper[i] == null && i < sizeOfTbl) { i++; }

        // we can choose any index from ix to sizeOfTable
        // but we must leave space for elements from ix to size
        if (i == sizeOfTbl) {
            int newIx = Engine.makeSymbolicInt(); // "any index"
            Engine.assume(ix <= newIx); // lower border
            Engine.assume(newIx < sizeOfTbl - size + ix); // space to higher indexes
            T t = table.getEnsure(newIx);
            mapper[newIx] = ix; // note index
            Engine.assume(filter.test(t));
            return t;
        }

        // found self -> can just return
        if (mapper[i] == ix) return table.getEnsure(i);

        // found one of lower indexes
        // need to skip all them all to find self
        int toSkip = ix - mapper[i] - 1;
        int ii = i + 1;
        while(toSkip != -1 && ii < sizeOfTbl) {
            if (mapper[ii] != null) toSkip--;
            ii++;
        }

        // fond self
        if (toSkip == -1) {
            assert(ix == mapper[ii - 1]);
            return table.getEnsure(ii - 1);
        }

        // like last time
        assert(ii == sizeOfTbl);
        int newIx = Engine.makeSymbolicInt();
        Engine.assume(i + 1 <= newIx);
        Engine.assume(newIx < sizeOfTbl - toSkip);
        T t = table.getEnsure(newIx);
        mapper[newIx] = ix;
        Engine.assume(filter.test(t));
        return t;
    }

    @Override
    public int indexIn(T t, int startIx, int endIx) {
        for (int i = startIx; i < endIx; i++) {
            T tt = getEnsure(i);
            if (t == tt || t.equals(tt)) { return i; }
        }
        return -1;
    }

    @Override
    public boolean containsIn(T t, int startIx, int endIx) {
        return indexIn(t, startIx, endIx) != -1;
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public ITable<T> clone() {
        return new FiltredTable<>(
                table.clone(),
                sizeOfTbl,
                size,
                mapper,
                filter
        );
    }

    public T firstEnsure() {
        Engine.assume(size > 0);
        return getEnsure(0);
    }
}
