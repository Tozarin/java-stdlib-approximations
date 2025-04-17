package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;
import org.usvm.api.SymbolicMap;

import java.util.*;

public class BaseTable<V> extends ABaseTable<V> {
    //      row0  row2  --  rowN
    //  col0
    //  col1
    //   --
    //  colM
    public Object[][] data;
    public int size;

    public int columnCount;
    public int idIndex;
    public Class<?>[] columnTypes;

    public BaseTable(
            int idIndex,
            Class<?>... columnTypes) {

        this.columnTypes = columnTypes;
        this.columnCount = columnTypes.length;
        this.idIndex = idIndex;
        this.size = Engine.makeSymbolicInt();
        Engine.assume(size > -1);

        this.data = new Object[columnCount][];

        for (int i = 0; i < columnCount; i++) { data[i] = Engine.makeSymbolicArray(columnTypes[i], size); }
    }

    @Override
    public int size() { return size; }

    @Override
    public int idColumnIx() { return idIndex; }

    @Override
    public int columnCount() { return columnCount; }

    @Override
    public Class<?>[] columnTypes() { return columnTypes; }

    @Override
    public Class<Object[]> type() { return Object[].class; }

    @Override
    public void deleteAll() {
        for (int i = 0; i < columnCount; i++) { data[i] = Engine.makeSymbolicArray(columnTypes[i], 0); }
        this.size = 0;
    }

    public Object[] getRowEnsure(int ix) {
        Engine.assume(ix < size);
        Object[] row = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            if (i == idIndex) Engine.assume(row[i] != null);
            row[i] = data[i][ix];
        }
        return row;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() { return new BaseTableIterator<>(this); }

    @Override
    public Iterator<Object[]> backIterator() { return new BaseTableIterator<>(this, true); }
}

// V --- type of id field
//public class BaseTable<V> extends ABaseTable<V> {
//
//    //      row0  row2  --  rowN
//    //  col0
//    //  col1
//    //   --
//    //  colM
//    public Object[][] data;
//    public int size;
//
//    public SymbolicMap<V, Integer> ids;
//
//    public int columnCount;
//    public int idIndex;
//    public Class<?>[] columnTypes;
//
//    public Set<Integer> removedIx;
//    public int countOfRemoved;
//
//    public BaseTable(
//            int idIndex,
//            Class<?>... columnTypes) {
//
//        this.columnTypes = columnTypes;
//        this.columnCount = columnTypes.length;
//        this.idIndex = idIndex;
//        this.size = Engine.makeSymbolicInt();
//        Engine.assume(size > -1);
//
//        this.data = new Object[columnCount][];
//
//        for (int i = 0; i < columnCount; i++) { data[i] = Engine.makeSymbolicArray(columnTypes[i], size); }
//
//        this.ids = Engine.makeFullySymbolicMap();
//        Engine.assume(ids != null);
//        Engine.assume(ids.size() == size);
//
//        this.removedIx = new HashSet<>();
//        this.countOfRemoved = 0;
//    }
//
//    public BaseTable(
//            Object[][] data,
//            int size,
//            SymbolicMap<V, Integer> ids,
//            int columnCount,
//            int idIndex,
//            Set<Integer> removedIx,
//            int countOfRemoved
//    ) {
//        this.data = data;
//        this.size = size;
//        this.ids = ids;
//        this.columnCount = columnCount;
//        this.idIndex = idIndex;
//        this.removedIx = removedIx;
//        this.countOfRemoved = countOfRemoved;
//    }
//
//    public Integer getIndexFromMap(V id) {
//        Object mapId = ids.get(id);
//        Engine.assume(mapId != null);
//        Engine.assume(mapId instanceof Integer);
//        return (Integer) mapId;
//    }
//
//    @Override
//    public int size() {
//        return size - countOfRemoved;
//    }
//
//    @Override
//    public int idColumnIx() { return idIndex; }
//
//    @Override
//    public int columnCount() { return columnCount; }
//
//    @Override
//    public Class<?>[] columnTypes() { return columnTypes; }
//
//    public Object[] getRow(int ix) {
//
//        Object[] row = new Object[columnCount];
//        for (int i = 0; i < columnCount; i++) {
//            Engine.assume(ix < data[i].length);
//            row[i] = data[i][ix];
//        }
//
//        return row;
//    }
//
//    @SuppressWarnings("unchecked")
//    public Object[] getEnsure(int ix) {
//
//        Engine.assume(ix < size());
//        Object[] row = getRow(ix);
//        V id = (V) row[idIndex];
//
//        Engine.assume(ids.containsKey(id));
//        Integer mapId = getIndexFromMap(id);
//        Engine.assume(mapId == ix);
//
//        return row;
//    }
//
//    @NotNull
//    @Override
//    public Iterator<Object[]> iterator() {
//        return new BaseTableIterator<>(this);
//    }
//
//    @NotNull
//    @Override
//    public Iterator<Object[]> backIterator() {
//        return new BaseTableIterator<>(this, true);
//    }
//
//    @Override
//    public Class<Object[]> type() {
//        return Object[].class;
//    }
//
//    @Override
//    public Object[] first() {
//        if (size != 0) return getEnsure(0);
//        return null;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void save(Object[] row) {
//        V id = (V) row[idIndex];
//
//        if (ids.containsKey(id)) {
//            int ix = getIndexFromMap(id);
//
////            Object[] currRow = getRow(ix);
////            Object[] merged = mergeRows(currRow, row);
//
//            for (int i = 0; i < columnCount; i++) {
//                data[i][ix] = row[i];
//            }
//
//            if (removedIx.add(ix)) countOfRemoved--;
//            return;
//        }
//
//        for (int i = 0; i < columnCount; i++) {
//            Engine.assume(data[i].length == size);
//            data[i] = Arrays.copyOf(data[i], size + 1);
//            Engine.assume(data[i].length == size + 1);
//            data[i][size] = row[i];
//        }
//
//        ids.set(id, size);
//        size++;
//    }
//
//    public void saveAll(Iterable<Object[]> rows) {
//        for (Object[] row : rows) {
//            save(row);
//        }
//    }
//
//    @Override
//    public void deleteById(V id) {
//        if (ids.containsKey(id)) {
//            int ix = getIndexFromMap(id);
//            if (removedIx.add(ix)) countOfRemoved++;
//        }
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void deleteAll() {
//        data = new Object[countOfRemoved][0];
//        size = 0;
//        ids = Engine.makeSymbolicMap();
//        Engine.assume(ids != null);
//        Engine.assume(ids.size() == 0);
//        removedIx = new HashSet<>();
//        countOfRemoved = 0;
//    }
//
//    @Override
//    public boolean existsById(V key) {
//        if (!ids.containsKey(key)) return false;
//        int id = getIndexFromMap(key);
//        return !removedIx.contains(id);
//    }
//
//    // need crudManager
//    @Override
//    public Optional<Object[]> findById(V key) {
//        if (!existsById(key)) return Optional.empty();
//        int id = getIndexFromMap(key);
//        return Optional.of(getRow(id));
//    }
//
//    // need crudManager
//    @Override
//    public Iterable<Object[]> findAllById(Iterable<V> keys) {
//        class FindAllByIdIterable implements Iterable<Object[]> {
//            class FindAllByIdIterator implements Iterator<Object[]> {
//                Iterator<V> keysIter;
//                V currKey;
//
//                public FindAllByIdIterator() {
//                    this.keysIter = keys.iterator();
//                    this.currKey = null;
//                }
//
//                @Override
//                public boolean hasNext() {
//                    if (currKey != null) return true;
//
//                    while (keysIter.hasNext()) {
//                        V key = keysIter.next();
//                        if (ids.containsKey(key)) {
//                            currKey = key;
//                            return true;
//                        }
//                    }
//
//                    return false;
//                }
//
//                @Override
//                public Object[] next() {
//                    Engine.assume(hasNext());
//
//                    int ix = getIndexFromMap(currKey);
//                    currKey = null;
//
//                    return getRow(ix);
//                }
//            }
//
//            @NotNull
//            @Override
//            public Iterator<Object[]> iterator() {
//                return new FindAllByIdIterator();
//            }
//        }
//
//        return new FindAllByIdIterable();
//    }
//}
