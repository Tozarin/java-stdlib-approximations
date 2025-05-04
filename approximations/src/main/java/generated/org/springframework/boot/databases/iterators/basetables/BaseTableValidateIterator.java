package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTableValidate;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.function.Function;

public class BaseTableValidateIterator<V> implements Iterator<Object[]> {

    public BaseTableValidate<V> table;
    public Iterator<Object[]> tblIter;
    public Function<Object, Boolean>[] validators;

    public BaseTableValidateIterator(BaseTableValidate<V> table) {
        this.table = table;
        this.tblIter = table.table.iterator();
        this.validators = table.validators;
    }

    @Override
    public boolean hasNext() { return tblIter.hasNext(); }

    @Override
    public Object[] next() {
        Engine.assume(hasNext());

        Object[] row = tblIter.next();
        for (int i = 0; i < table.columnCount(); i++) {
            if (validators[i] != null) Engine.assume(validators[i].apply(row[i]));
        }

        return row;
    }
}
