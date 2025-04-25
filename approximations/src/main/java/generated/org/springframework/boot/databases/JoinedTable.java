package generated.org.springframework.boot.databases;

import generated.org.springframework.boot.databases.iterators.JoinedIterator;
import org.jetbrains.annotations.NotNull;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class JoinedTable<L, R> implements ITable<Object[]> {

    public int size;

    public ITable<L> leftTable;
    public ITable<R> rightTable;

    // nullable
    Function<L, Object[]> leftSerializer;
    Function<R, Object[]> rightSerializer;

    int rightSize; // fields count of right class

    // nullable
    Predicate<Object[]> onMethod;

    // true - JOIN LEFT, false - normal join, to RIGHT JOIN replace left and right tables
    public boolean isLeft;

    public JoinedTable(
            ITable<L> leftTable,
            ITable<R> rightTable,
            Function<L, Object[]> leftSerializer,
            Function<R, Object[]> rightSerializer,
            int rightSize,
            Predicate<Object[]> onMethod,
            boolean isLeft
    ) {
        this(-1, leftTable, rightTable, leftSerializer, rightSerializer, rightSize, onMethod, isLeft);
    }

    public JoinedTable(
            ITable<L> leftTable, // ITable<Object[]>
            ITable<R> rightTable, // ITable<Object[]>
            Predicate<Object[]> onMethod,
            boolean isLeft
    ) {
        this(-1, leftTable, rightTable, null, null, -1, onMethod, isLeft);
    }

    public JoinedTable(ITable<L> leftTable, ITable<R> rightTable) {
        this(leftTable, rightTable, null, false);
    }

    public JoinedTable(
            ITable<L> leftTable,
            ITable<R> rightTable,
            Function<L, Object[]> leftSerializer,
            Function<R, Object[]> rightSerializer
    ) {
        this(leftTable, rightTable, leftSerializer, rightSerializer, -1, null, false);
    }

    public JoinedTable(
            int size,
            ITable<L> leftTable,
            ITable<R> rightTable,
            Function<L, Object[]> leftSerializer,
            Function<R, Object[]> rightSerializer,
            int rightSize,
            Predicate<Object[]> onMethod,
            boolean isLeft
    ) {
        this.size = size;
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.leftSerializer = leftSerializer;
        this.rightSerializer = rightSerializer;
        this.rightSize = rightSize;
        this.onMethod = onMethod;
        this.isLeft = isLeft;
    }

    public boolean applyPredicate(Object[] row) {
        if (onMethod == null) return true;
        return onMethod.test(row);
    }

    public Object[] serializeLeft(L l) {
        if (leftSerializer == null) return (Object[]) l;
        return leftSerializer.apply(l);
    }

    public Object[] serializeRight(R r) {
        if (rightSerializer == null) return (Object[]) r;
        return r != null ? rightSerializer.apply(r) : new Object[rightSize];
    }

    // r is null when isLeft and onMethod is false for all rs
    public Object[] composite(L l, R r) {

        if (l != null && r != null) {

            Object[] lRow = serializeLeft(l);
            Object[] rRow = serializeRight(r);

            Object[] row = new Object[lRow.length + rRow.length];

            for (int i = 0; i < lRow.length; i++) row[i] = lRow[i];
            for (int i = 0; i < rRow.length; i++) row[i + lRow.length] = rRow[i];

            return row;
        }

        return null;
    }

    public int size() {
        if (size != -1) return size;

        if (onMethod == null) {
            size = leftTable.size() * rightTable.size();
            return size;
        }

        Iterator<Object[]> iter = new JoinedIterator<>(this);
        int count = 0;
        while (iter.hasNext()) {
            Object[] candidate = iter.next();
            Engine.assume(applyPredicate(candidate));
            count++;
        }

        size = count;
        return size;
    }

    @NotNull
    @Override
    public Iterator<Object[]> iterator() {
        return new JoinedIterator<>(this);
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }

    @Override
    public Object[] first() {
        if (onMethod == null) return composite(leftTable.first(), rightTable.first());

        Iterator<Object[]> iter = iterator();
        if (!iter.hasNext()) return null;
        return iter.next();
    }

    public static Object[] identity(Object[] row) {
        return row;
    }
}
