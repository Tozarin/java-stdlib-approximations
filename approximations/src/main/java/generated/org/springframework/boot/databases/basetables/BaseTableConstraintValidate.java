package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.iterators.basetables.BaseTableConstraintValidateIterator;
import jakarta.validation.ConstraintValidator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaseTableConstraintValidate<V> extends AChainedBaseTable<V> {

    public ConstraintValidator<?, ?>[][] validators;

    public BaseTableConstraintValidate(
            ABaseTable<V> table,
            ConstraintValidator<?, ?>[][] validators
    ) {
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
        return new BaseTableConstraintValidateIterator<>(this);
    }
}
