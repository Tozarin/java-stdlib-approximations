package generated.org.springframework.boot.databases.basetables;

import org.usvm.api.Engine;

import java.util.Optional;

// V - type of id field
public abstract class ABaseTable<V> extends ANoIdTable {
    public abstract int idColumnIx();
}
