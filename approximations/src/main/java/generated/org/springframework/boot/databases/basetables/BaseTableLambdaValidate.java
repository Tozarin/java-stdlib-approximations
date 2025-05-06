package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableLambdaValidateIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class BaseTableLambdaValidate<V> extends AChainedBaseTable<V> {

    public Function<Object, Boolean>[] validators;

    public BaseTableLambdaValidate(ABaseTable<V> table, Function<Object, Boolean>[] validators) {
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
        Iterator<Object[]> iter = iterator();
        while (iter.hasNext()) {
            Object[] ignored = iter.next();
            count++;
        }
        return count;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new BaseTableLambdaValidateIterator<>(this);
    }
}
