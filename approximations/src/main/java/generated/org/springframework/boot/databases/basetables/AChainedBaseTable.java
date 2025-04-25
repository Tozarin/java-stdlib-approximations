package generated.org.springframework.boot.databases.basetables;

public abstract class AChainedBaseTable<V> extends ABaseTable<V> {

    public ABaseTable<V> table;

    @Override
    public int idColumnIx() { return table.idColumnIx(); }

    @Override
    public int columnCount() { return table.columnCount(); }

    @Override
    public Class<?>[] columnTypes() { return table.columnTypes(); }

    @Override
    public Class<Object[]> type() { return Object[].class; }
}
