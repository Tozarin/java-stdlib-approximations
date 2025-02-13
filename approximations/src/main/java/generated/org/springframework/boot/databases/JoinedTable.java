package generated.org.springframework.boot.databases;

import org.usvm.api.Engine;
import runtime.LibSLRuntime;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class JoinedTable<L, R> implements ITable<Object[]> {

    public int size;

    public ITable<L> leftTable;
    public ITable<R> rightTable;

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
        return leftSerializer.apply(l);
    }

    public Object[] serializeRight(R r) {
        return r != null ? rightSerializer.apply(r) : new Object[rightSize];
    }

    // r is null when isLeft and onMethod is false for all rs
    public Object[] composite(L l, R r) {

        Object[] lRow = serializeLeft(l);
        Object[] rRow = serializeRight(r);

        Object[] row = new Object[lRow.length + rRow.length];
        LibSLRuntime.ArrayActions.copy(lRow, 0, row, 0, lRow.length);
        LibSLRuntime.ArrayActions.copy(rRow, 0, row, lRow.length, rRow.length);

        return row;
    }

    public int size() {
        if (size != -1) return size;

        if (onMethod == null) {
            size = leftTable.size() * rightTable.size();
            return size;
        }

        Iterator<Object[]> iter = new JoinedIterator();
        int count = 0;
        while (iter.hasNext()) {
            Object[] candidate = iter.next();
            Engine.assume(applyPredicate(candidate));
            count++;
        }

        size = count;
        return size;
    }

    class JoinedIterator implements Iterator<Object[]> {

        Iterator<L> leftIter;
        Iterator<R> rightIter;

        L currLeft;
        Object[] currComposited;

        boolean reversed;
        boolean isEmpty;
        boolean findedRight;

        public JoinedIterator() {
            this(false);
        }

        public JoinedIterator(boolean reversed) {
            this.reversed = reversed;
            this.findedRight = false;

            resetLeftIter();
            resetRightIter();

            this.isEmpty = !leftIter.hasNext() && !rightIter.hasNext();

            this.currLeft = isEmpty ? null : leftIter.next();
            this.currComposited = null;
        }

        private void resetLeftIter() {
            leftIter = reversed ? leftTable.clone().backIterator() : leftTable.clone().iterator();
        }

        private void resetRightIter() {
            rightIter = reversed ? rightTable.clone().backIterator() : rightTable.clone().iterator();
        }

        @Override
        public boolean hasNext() {
            if (isEmpty) return false;
            if (currComposited != null) return true;

            while (rightIter.hasNext()) {
                R right = rightIter.next();
                Object[] candidate = composite(currLeft, right);
                if (applyPredicate(candidate)) {
                    findedRight = true;
                    currComposited = candidate;
                    return true;
                }
            }

            if (isLeft && !findedRight) {
                findedRight = true;
                currComposited = composite(currLeft, null);
                return true;
            }

            if (!leftIter.hasNext()) return false;

            resetRightIter();
            currLeft = leftIter.next();
            findedRight = false;

            return hasNext();
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();

            Object[] tmp = currComposited;
            currComposited = null;
            return tmp;
        }
    }

    public Iterator<Object[]> iterator() {
        return new JoinedIterator();
    }

    public Iterator<Object[]> backIterator() {
        return new JoinedIterator(true);
    }

    public Class<Object[]> type() {
        return Object[].class;
    }

    public ITable<Object[]> clone() {
        return new JoinedTable<>(
                size,
                leftTable.clone(),
                rightTable.clone(),
                leftSerializer,
                rightSerializer,
                rightSize,
                onMethod,
                isLeft
        );
    }
}
