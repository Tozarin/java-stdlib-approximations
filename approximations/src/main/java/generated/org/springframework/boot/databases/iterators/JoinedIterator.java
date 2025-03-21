package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.JoinedTable;
import org.usvm.api.Engine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class JoinedIterator<L, R> implements Iterator<Object[]> {

    JoinedTable<L, R> joinedTable;

    Iterator<L> leftIter;
    Iterator<R> rightIter;

    L currLeft;
    Object[] currComposited;

    boolean reversed;
    boolean isEmpty;
    boolean findedRight;

    public JoinedIterator(JoinedTable<L, R> joinedTable) {
        this(joinedTable, false);
    }

    public JoinedIterator(JoinedTable<L, R> joinedTable, boolean reversed) {
        this.joinedTable = joinedTable;

        this.reversed = reversed;
        this.findedRight = false;

        resetLeftIter();
        resetRightIter();

        this.isEmpty = !leftIter.hasNext() && !rightIter.hasNext();

        this.currLeft = isEmpty ? null : leftIter.next();
        this.currComposited = null;
    }

    private void resetLeftIter() {
        leftIter = reversed ? joinedTable.leftTable.backIterator() : joinedTable.leftTable.iterator();
    }

    private void resetRightIter() {
        rightIter = reversed ? joinedTable.rightTable.backIterator() : joinedTable.rightTable.iterator();
    }

    @Override
    public boolean hasNext() {
        if (currComposited != null) return true;
        if (!isEmpty) return notEmptyHasNext();

        return false;
    }

    private boolean notEmptyHasNext() {
        while (rightIter.hasNext()) {
            R right = rightIter.next();
            Object[] candidate = joinedTable.composite(currLeft, right);
            if (joinedTable.applyPredicate(candidate)) {
                findedRight = true;
                currComposited = candidate;
                return true;
            }
        }

        if (joinedTable.isLeft && !findedRight) {
            findedRight = true;
            currComposited = joinedTable.composite(currLeft, null);
            return true;
        }

        if (leftIter.hasNext()) {
            resetRightIter();
            currLeft = leftIter.next();
            findedRight = false;

            return hasNext();
        }

        return false;
    }

    @Override
    public Object[] next() {
        if (!hasNext()) throw new NoSuchElementException();

        Object[] tmp = currComposited;
        currComposited = null;
        return tmp;
    }
}
