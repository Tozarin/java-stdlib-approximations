package generated.org.springframework.boot.databases.basetables;

public abstract class AChainedNoIdTable extends ANoIdTable {

    public ANoIdTable table;

    @Override
    public int columnCount() {
        return table.columnCount();
    }

    @Override
    public Class<?>[] columnTypes() {
        return table.columnTypes();
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }
}
