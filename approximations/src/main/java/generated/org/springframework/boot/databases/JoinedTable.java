package generated.org.springframework.boot.databases;

import runtime.LibSLRuntime;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class JoinedTable<L, R> implements ITable<Object[]> {

    public ITable<L> leftTable;
    public ITable<R> rightTable;

    Function<L, Object[]> leftSerializer;
    Function<R, Object[]> rightSerializer;

    public JoinedTable(
            ITable<L> leftTable,
            ITable<R> rightTable,
            Function<L, Object[]> leftSerializer,
            Function<R, Object[]> rightSerializer
    ) {
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.leftSerializer = leftSerializer;
        this.rightSerializer = rightSerializer;
    }

    public int size() {
        return leftTable.size() * rightTable.size();
    }

    class JoinedIterator implements Iterator<Object[]> {

        Iterator<L> leftIter;
        Iterator<R> rightIter;

        L currLeft;

        boolean isEmpty;

        boolean reversed;

        public JoinedIterator() {
            this(false);
        }

        public JoinedIterator(boolean reversed) {
            this.reversed = reversed;
            resetLeftIter();
            resetRightIter();

            this.isEmpty = !leftIter.hasNext() || !rightIter.hasNext();
            this.currLeft = leftIter.next();
        }

        private void resetLeftIter() {
            if (reversed) leftIter = leftTable.clone().backIterator();
            leftIter = leftTable.clone().iterator();
        }

        private void resetRightIter() {
            if (reversed) rightIter = rightTable.clone().backIterator();
            rightIter = rightTable.clone().iterator();
        }

        @Override
        public boolean hasNext() {
            if (isEmpty) return false;

            return leftIter.hasNext() || rightIter.hasNext();
        }

        @Override
        public Object[] next() {
            if (!hasNext()) throw new NoSuchElementException();

            if (!rightIter.hasNext()) {
                currLeft = leftIter.next();
                resetRightIter();
            }

            R currRight = rightIter.next();

            return composite(currLeft, currRight);
        }
    }

    public Object[] composite(L l, R r) {

        Object[] lRow = leftSerializer.apply(l);
        Object[] rRow = rightSerializer.apply(r);

        Object[] row = new Object[lRow.length + rRow.length];
        LibSLRuntime.ArrayActions.copy(lRow, 0, row, 0, lRow.length);
        LibSLRuntime.ArrayActions.copy(rRow, 0, row, lRow.length, rRow.length);

        return row;
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
                leftTable.clone(),
                rightTable.clone(),
                leftSerializer,
                rightSerializer
        );
    }
}
