package generated.org.springframework.boot.databases.basetables;

import java.util.Iterator;
import java.util.Optional;

// V - type of id field
public abstract class ABaseTable<V> extends ANoIdTable {
    public abstract int idColumnIx();


    // TODO [OPT]: optimize everywhere
    public Optional<Object[]> findById(V id) {
        Iterator<Object[]> iter = iterator();
        while (iter.hasNext()) {
            Object[] row = iter.next();
            if (row[idColumnIx()].equals(id)) return Optional.of(row);
        }
        return Optional.empty();
    }
}
