package generated.org.springframework.boot.databases;

import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

// T - type of dataclass, V - type of id field
public class CrudManager<T, V> {

    public BaseTable<V> table;
    public Function<T, Object[]> serializer; // nullable
    public Function<Object[], T> deserializer; // nullable

    public CrudManager(
            BaseTable<V> table,
            Function<T, Object[]> serializer,
            Function<Object[], T> deserializer
    ) {
        this.table = table;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public T save(T t) {
        Object[] row = serializer.apply(t);
        table.save(row);
        return t;
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

    public Iterable<T> findAll() {
        return new IterableWithMap<>(table.findAll(), deserializer);
    }

    public Optional<T> findById(V key) {
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

        class IteratorWithMap implements Iterator<R> {

            Iterator<I> iter;

            public IteratorWithMap() {
                this.iter = iterable.iterator();
            }

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iter.next());
            }
        }

        @NotNull
        @Override
        public Iterator<R> iterator() {
            return new IteratorWithMap();
        }
    }
}
