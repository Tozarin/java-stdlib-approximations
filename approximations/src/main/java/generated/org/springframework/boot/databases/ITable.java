package generated.org.springframework.boot.databases;

import java.util.Iterator;

public interface ITable<T> extends Iterable<T> {

    int size();

    Class<T> type();

    T first();
}
