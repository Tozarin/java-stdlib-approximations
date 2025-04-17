package generated.org.springframework.boot.databases.saveupddel;

import generated.org.springframework.boot.databases.basetables.BaseTable;
import generated.org.springframework.boot.databases.MappedTable;
import generated.org.springframework.boot.databases.basetables.BaseTableManager;
import generated.org.springframework.boot.databases.iterators.utils.IteratorWithMap;
import generated.org.springframework.boot.databases.wrappers.ListWrapper;
import generated.org.springframework.boot.databases.wrappers.SetWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

// T - type of dataclass, V - type of id field
public class CrudManager<T, V> {

    public BaseTableManager<V> table;
    public Function<T, Object[]> serializer; // nullable
    public Function<Object[], T> deserializer; // nullable

    public Class<T> genericType;

    public CrudManager(
            BaseTableManager<V> table,
            Function<T, Object[]> serializer,
            Function<Object[], T> deserializer,
            Class<T> genericType
    ) {
        this.table = table;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.genericType = genericType;
    }

    @SuppressWarnings("unchecked")
    // allowUpdate - may or not update given entity in database
    public void save(T t, boolean allowUpdate) {
        Object[] row = serializer.apply(t);
        V id = (V) row[table.idColumnIx()];

        if (!allowUpdate && existById(id)) return;

        table.save(row);
    }

    public Iterable<? extends T> saveAll(Iterable<? extends T> ts) {
        for (T t : ts) {
            Object[] row = serializer.apply(t);
            table.save(row);
        }
        return ts;
    }

    public void delete(T t) {
        Object[] row = serializer.apply(t);
        table.delete(row);
    }

    public void deleteAll() {
        table.deleteAll();
    }

    public void deleteAll(Iterable<? extends T> ts) {
        for (T t : ts) {
            Object[] row = serializer.apply(t);
            table.delete(row);
        }
    }

    public void deleteAllById(Iterable<? extends V> keys) {
        table.deleteAllById(keys);
    }

    public boolean existById(V key) {
        return table.existsById(key);
    }

    // It is important that names of following template
    // [name of repository method]_[return type where "." replaced with "_"]
    public Iterable<T> findAll_java_lang_Iterable() {
        return new IterableWithMap<>(table.findAll(), deserializer);
    }

    public List<T> findAll_java_util_List() {
        MappedTable<Object[], T> mapped = new MappedTable<>(table, deserializer, genericType);
        return new ListWrapper<>(mapped);
    }

    public Set<T> findAll_java_util_Set() {
        MappedTable<Object[], T> mapped = new MappedTable<>(table, deserializer, genericType);
        return new SetWrapper<>(mapped);
    }

    public T findById_T(V key) {
        Optional<Object[]> row = table.findById(key);
        return row.map(deserializer).orElse(null);
    }

    public Optional<T> findById_java_util_Optional(V key) {
        return table.findById(key).map(deserializer);
    }

    public Iterable<T> findAllById(Iterable<V> keys) {
        return new IterableWithMap<>(table.findAllById(keys), deserializer);
    }

    class IterableWithMap<I, R> implements Iterable<R> {

        Iterable<I> iterable;
        Function<I, R> mapper;

        public IterableWithMap(Iterable<I> iterable, Function<I, R> mapper) {
            this.iterable = iterable;
            this.mapper = mapper;
        }

        @NotNull
        @Override
        public Iterator<R> iterator() {
            return new IteratorWithMap<>(iterable.iterator(), mapper);
        }
    }
}
