package generated.org.springframework.boot.databases;

import kotlin.jvm.functions.Function2;
import org.usvm.api.Engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FiltredTable<T> implements ITable<T> {

    public ITable<T> table;
    public Function2<T, Object[], Boolean> filter;

    public List<T> cache;
    public int cacheSize;

    // arguments of original repository method
    Object[] methodArgs;

    public FiltredTable(ITable<T> table, Function2<T, Object[], Boolean> filter, Object[] methodArgs) {
        this.table = table;
        this.filter = filter;

        this.cache = new ArrayList<>();
        this.cacheSize = -1;

        this.methodArgs = methodArgs;
    }

    public FiltredTable(ITable<T> table, Function2<T, Object[], Boolean> filter) {
        this(table, filter, new Object[0]);
    }

    public FiltredTable(
            ITable<T> table,
            Function2<T, Object[], Boolean> filter,
            List<T> cache,
            int cacheSize,
            Object[] methodArgs
    ) {
        this.table = table;
        this.filter = filter;
        this.cache = cache;
        this.cacheSize = cacheSize;
        this.methodArgs = methodArgs;
    }

    public boolean callFilter(T t) {
        return filter.invoke(t, methodArgs);
    }

    public int size() {

        if (cacheSize != -1) return cacheSize;

        Iterator<T> iter = iterator();
        int count = 0;
        while (iter.hasNext()) {
            T candidate = iter.next();
            if (callFilter(candidate)) {
                count++;
                cache.add(candidate);
            }
        }

        cacheSize = count;
        return cacheSize;
    }

    class FiltredIterator implements Iterator<T> {

        Iterator<T> tblIter;
        T curr;

        public FiltredIterator() {
            this.tblIter = table.clone().iterator();
            this.curr = null;
        }

        public FiltredIterator(Iterator<T> tblIter) {
            this.tblIter = tblIter;
            this.curr = null;
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            while (tblIter.hasNext()) {
                T candidate = tblIter.next();
                if (callFilter(candidate)) {
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

    public Iterator<T> iterator() {
        if (cacheSize != -1) return cache.iterator();
        return new FiltredIterator();
    }

    public Iterator<T> backIterator() {
        return new FiltredIterator(table.clone().backIterator());
    }

    public Class<T> type() {
        return table.type();
    }

    public ITable<T> clone() {
        return new FiltredTable<>(
                table.clone(),
                filter,
                cache,
                cacheSize,
                methodArgs
        );
    }

    public T firstEnsure() {
        Iterator<T> iter = iterator();
        Engine.assume(iter.hasNext());
        return iter.next();
    }
}
