package generated.org.springframework.boot.databases;

import java.util.Iterator;

public interface ITable<T> extends Iterable<T> {

    int size();

    Iterator<T> backIterator();

    Class<T> type();

    ITable<T> clone();
}
