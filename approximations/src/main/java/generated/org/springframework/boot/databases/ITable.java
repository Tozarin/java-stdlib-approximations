package generated.org.springframework.boot.databases;

import java.util.Iterator;

public interface ITable<T> {

    int size();

    Iterator<T> iterator();

    Iterator<T> backIterator();

    Class<T> type();

    ITable<T> clone();
}
