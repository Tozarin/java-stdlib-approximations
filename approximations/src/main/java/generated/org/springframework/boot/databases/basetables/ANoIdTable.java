package generated.org.springframework.boot.databases.basetables;

import generated.org.springframework.boot.databases.ITable;

import java.util.Iterator;

public abstract class ANoIdTable implements ITable<Object[]> {

    public abstract int columnCount();

    public abstract Class<?>[] columnTypes();

    public abstract void deleteAll();

    public Iterable<Object[]> findAll() {
        return this;
    }

    @Override
    public Object[] first() {
        Iterator<Object[]> iter = iterator();
        if (iter.hasNext()) return iter.next();
        return null;
    }

    public boolean rowEquals(Object[] left, Object[] right) {
        for (int i = 0; i < columnCount(); i++) {
            if (!left[i].equals(right[i])) return false;
        }
        return true;
    }
}
