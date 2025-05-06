package generated.org.springframework.boot.databases.iterators.basetables;

import generated.org.springframework.boot.databases.basetables.BaseTableLambdaValidate;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.function.Function;

public class BaseTableLambdaValidateIterator<V> implements Iterator<Object[]> {

    public BaseTableLambdaValidate<V> table;
    public Iterator<Object[]> tblIter;
    public Function<Object, Boolean>[] validators;

    public BaseTableLambdaValidateIterator(BaseTableLambdaValidate<V> table) {
        this.table = table;
        this.tblIter = table.table.iterator();
        this.validators = table.validators;
    }

    @Override
    public boolean hasNext() {
        return tblIter.hasNext();
    }

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
