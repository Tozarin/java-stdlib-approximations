package generated.org.springframework.boot.databases;

import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.*;

// V --- type of id field
public class BaseTable<V> implements ITable<Object[]> {

    //      row0  row2  --  rowN
    //  col0
    //  col1
    //   --
    //  colM
    public Object[][] data;
    public int size;

    public SymbolicMap<V, Integer> ids;

    public int columnCount;
    public int idIndex;

    public Set<Integer> removedIx;
    public int countOfRemoved;

    public BaseTable(
            int idIndex,
            Class<?>... columnTypes) {

        this.columnCount = columnTypes.length;
        this.idIndex = idIndex;
        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);

        this.data = new Object[columnCount][];

        for (int i = 0; i < columnCount; i++) {
            data[i] = Engine.makeSymbolicArray(columnTypes[i], size);
            Engine.assume(data[i] != null);
        }

        this.ids = Engine.makeSymbolicMap();
        Engine.assume(ids != null);
        Engine.assume(ids.size() == size);

        this.removedIx = new HashSet<>();
        this.countOfRemoved = 0;
    }

    public BaseTable(
            Object[][] data,
            int size,
            SymbolicMap<V, Integer> ids,
            int columnCount,
            int idIndex,
            Set<Integer> removedIx,
            int countOfRemoved
    ) {
        this.data = data;
        this.size = size;
        this.ids = ids;
        this.columnCount = columnCount;
        this.idIndex = idIndex;
        this.removedIx = removedIx;
        this.countOfRemoved = countOfRemoved;
    }

    @Override
    public int size() {
        return size - countOfRemoved;
    }

    public Object[] getRow(int ix) {

        Object[] row = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            row[i] = data[i][ix];
        }

        return row;
    }

    @SuppressWarnings("unchecked")
    public Object[] getEnsure(int ix) {

        Engine.assume(ix < size());
        Object[] row = getRow(ix);
        V id = (V) row[idIndex];
        Engine.assume(ids.containsKey(id));
        Engine.assume(ids.get(id) == ix);

        return row;
    }

    class BaseTableIterator implements Iterator<Object[]> {

        int ix;
        int endIx;

        Object[] curr;

        public BaseTableIterator() {
            this.ix = 0;
            this.endIx = size();

            this.curr = null;
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            while (removedIx.contains(ix)) {
                ix++;
            }

            if (endIx <= ix) return false;
            curr = getEnsure(ix++);

            return true;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();

            Object[] tmp = curr;
            curr = null;

            return tmp;
        }
    }

    class BaseTableBackIterator implements Iterator<Object[]> {

        int ix;

        Object[] curr;

        public BaseTableBackIterator() {
            this.ix = size() - 1;
            this.curr = null;
        }

        @Override
        public boolean hasNext() {

            if (curr != null) return true;

            while (removedIx.contains(ix)) {
                ix--;
            }
            if (ix < 0) return false;

            curr = getEnsure(ix--);

            return true;
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();

            Object[] tmp = curr;
            curr = null;

            return tmp;
        }
    }

    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableIterator();
    }

    @Override
    public Iterator<Object[]> backIterator() {
        return new BaseTableBackIterator();
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    @Override
    public ITable<Object[]> clone() {
        return new BaseTable<>(
                data,
                size,
                ids,
                columnCount,
                idIndex,
                new HashSet<>(removedIx),
                countOfRemoved
        );
    }

    @SuppressWarnings("unchecked")
    public void save(Object[] row) {
        V id = (V) row[idIndex];
        if (ids.containsKey(id)) {
            int ix = ids.get(id);
            for (int i = 0; i < columnCount; i++) {
                data[i][ix] = row[ix];
            }

            if (removedIx.add(ix)) countOfRemoved--;
        }

        for (int i = 0; i < columnCount; i++) {
            data[i] = Arrays.copyOf(data[i], size + 1);
            data[i][size] = row[i];
        }

        ids.set(id, size);
        size++;
    }

    public void saveAll(Iterable<Object[]> rows) {
        for (Object[] row : rows) {
            save(row);
        }
    }

    public void deleteById(V id) {
        if (ids.containsKey(id)) {
            int ix = ids.get(id);
            if (removedIx.add(ix)) countOfRemoved++;
        }
    }

    // need crudManager
    @SuppressWarnings("unchecked")
    public void delete(Object[] row) {
        V id = (V) row[idIndex];
        deleteById(id);
    }

    public void deleteAll() {
        data = new Object[countOfRemoved][0];
        size = 0;
        ids = Engine.makeSymbolicMap();
        Engine.assume(ids != null);
        Engine.assume(ids.size() == 0);
        removedIx = new HashSet<>();
        countOfRemoved = 0;
    }

    // use crudManager
    // public void deleteAll(Iterable<? extends T> es)

    public void deleteAllById(Iterable<? extends V> keys) {
        for (V id : keys) {
            deleteById(id);
        }
    }

    public boolean existsById(V key) {
        if (!ids.containsKey(key)) return false;
        int id = ids.get(key);
        return !removedIx.contains(id);
    }

    // need crudManager
    public Iterable<Object[]> findAll() {
        return this;
    }

    // need crudManager
    public Optional<Object[]> findById(V key) {
        if (!existsById(key)) return Optional.empty();
        int id = ids.get(key);
        return Optional.of(getRow(id));
    }

    // need crudManager
    public Iterable<Object[]> findAllById(Iterable<V> keys) {
        class FindAllByIdIterable implements Iterable<Object[]> {
            class FindAllByIdIterator implements Iterator<Object[]> {
                Iterator<V> keysIter;
                V currKey;

                public FindAllByIdIterator() {
                    this.keysIter = keys.iterator();
                    this.currKey = null;
                }

                @Override
                public boolean hasNext() {
                    if (currKey != null) return true;

                    while (keysIter.hasNext()) {
                        V key = keysIter.next();
                        if (ids.containsKey(key)) {
                            currKey = key;
                            return true;
                        }
                    }

                    return false;
                }

                @Override
                public Object[] next() {
                    if (!hasNext()) throw new NoSuchElementException();

                    int ix = ids.get(currKey);
                    currKey = null;

                    return getRow(ix);
                }
            }

            @NotNull
            @Override
            public Iterator<Object[]> iterator() {
                return new FindAllByIdIterator();
            }
        }

        return new FindAllByIdIterable();
    }
}
