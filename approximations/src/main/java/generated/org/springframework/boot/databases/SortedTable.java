package generated.org.springframework.boot.databases;

import kotlin.jvm.functions.Function2;

public class SortedTable<T> implements ITable<T> {

    public ITable<T> table;
    public int size;
    public int limit; // -1 if no limit
    public int offset; // 0 if no offset

    public Integer[] sorted; // from new indexes to old
    public Integer[] backSorted; // from old to new

    Function2<T, T, Boolean> comparer; // >

    public SortedTable(ITable<T> table, int limit, int offset, Function2<T, T, Boolean> comparer) {

        this.table = table;
        this.comparer = comparer;
        this.limit = limit;
        this.offset = offset;

        int tblSize = table.size();

        if (limit == -1) {
            this.size = tblSize - offset;
        }
        else {
            this.size = Math.min(tblSize - offset, limit);
        }

        this.sorted = new Integer[tblSize];
        this.backSorted = new Integer[tblSize];
        Sort();
    }

    public SortedTable(
            ITable<T> table,
            int limit,
            int offset,
            int size,
            Function2<T, T, Boolean> comparer,
            Integer[] sorted,
            Integer[] backSorted
    ) {
        this.table = table;
        this.limit = limit;
        this.offset = offset;
        this.size = size;
        this.comparer = comparer;
        this.sorted = sorted;
        this.backSorted = backSorted;
    }

    public void Sort() {
        int tblSize = sorted.length;
        for (int i = 0; i < tblSize; i++) {
            boolean swapped = false;
            for (int j = 0; j < tblSize - i - 1; j++) {

                if (sorted[j] == null) { sorted[j] = j; }
                if (sorted[j + 1] == null) { sorted[j + 1] = j + 1; }

                T ii = table.getEnsure(sorted[j]);
                T jj = table.getEnsure(sorted[j + 1]);

                // >
                if (comparer.invoke(ii, jj)) {
                    Integer tmpi = sorted[j];       // a
                    Integer tmpip = sorted[j + 1];  // b

                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = tmpi;

                    if (backSorted[tmpi] == null) { backSorted[tmpi] = tmpi; }
                    if (backSorted[tmpip] == null) { backSorted[tmpip] = tmpip; }

                    // sorted before |  back    | back     |
                    //  swap         |  before  | after    |
                    //   j   j+1     |  a    b  |  a    b  |
                    //   a    b      |  j   j+1 | j+1   j  |
                    Integer btmp = backSorted[tmpi];
                    backSorted[tmpi] = backSorted[tmpip];
                    backSorted[tmpip] = btmp;

                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public int ConvertToOldIx(int ix) {
        return ix + offset;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T getEnsure(int ix) {
        int oldIx = sorted[ConvertToOldIx(ix)];
        return table.getEnsure(oldIx);
    }

    @Override
    public int indexIn(T t, int startIx, int endIx) {
        int oldIx = table.indexIn(t, ConvertToOldIx(startIx), ConvertToOldIx(endIx));
        if (oldIx < 0) return oldIx;
        return backSorted[oldIx];
    }

    @Override
    public boolean containsIn(T t, int startIx, int endIx) {
        return indexIn(t, startIx, endIx) != -1;
    }

    @Override
    public Class<T> type() {
        return table.type();
    }

    @Override
    public ITable<T> clone() {
        return new SortedTable<>(
                table.clone(),
                limit,
                offset,
                size,
                comparer,
                sorted,
                backSorted
        );
    }
}
