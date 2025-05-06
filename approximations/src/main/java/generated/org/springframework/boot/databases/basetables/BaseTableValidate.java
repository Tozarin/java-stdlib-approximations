package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableValidateIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class BaseTableValidate<V> extends AChainedBaseTable<V> {

    public Function<Object, Boolean>[] validators;

    public BaseTableValidate(ABaseTable<V> table, Function<Object, Boolean>[] validators) {
        this.table = table;
        this.validators = validators;
    }

    @Override
    public void deleteAll() {
        table.deleteAll();
    }

    @Override
    public int size() {
        int count = 0;
        for (Object[] ignored : this) count++;
        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableValidateIterator<>(this);
    }
}
