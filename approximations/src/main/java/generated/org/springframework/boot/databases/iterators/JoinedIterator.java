package generated.org.springframework.boot.databases.iterators;

import generated.org.springframework.boot.databases.JoinedTable;
import org.usvm.api.Engine;

import java.util.Iterator;

public class JoinedIterator<L, R> implements Iterator<Object[]> {

    JoinedTable<L, R> joinedTable;

    Iterator<L> leftIter;
    Iterator<R> rightIter;

    L currLeft;
    Object[] currComposited;

    boolean isEmpty;
    boolean findedRight;

    public JoinedIterator(JoinedTable<L, R> joinedTable) {
        this.joinedTable = joinedTable;

        this.findedRight = false;

        resetLeftIter();
        resetRightIter();

        this.isEmpty = !leftIter.hasNext() && !rightIter.hasNext();

        this.currLeft = isEmpty ? null : leftIter.next();
        this.currComposited = null;
    }

    private void resetLeftIter() { leftIter = joinedTable.leftTable.iterator(); }

    private void resetRightIter() { rightIter = joinedTable.rightTable.iterator(); }

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
        Engine.assume(hasNext());

        Object[] tmp = currComposited;
        currComposited = null;
        return tmp;
    }
}
