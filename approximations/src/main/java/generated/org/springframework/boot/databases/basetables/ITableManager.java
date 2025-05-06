package generated.org.springframework.boot.databases.basetables;

import java.util.function.Function;

public interface ITableManager {
    void applyRangeUpdate(Function<Object[], Boolean> predicate, Function<Object[], Object[]> update);

    void applyRangeDelete(Function<Object[], Boolean> predicate);

    void save(Object[] row);

    void delete(Object[] row);
}
