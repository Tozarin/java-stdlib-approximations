package generated.org.springframework.boot.databases;

public interface ITable<T> extends Iterable<T> {

    int size();

    Class<T> type();

    T first();
}
