package generated.org.springframework.boot.databases;

public interface ITable<T> {

    int size();
    T getEnsure(int ix);
    int indexIn(T t, int startIx, int endIx);
    boolean containsIn(T t, int startIx, int endIx);
    Class<T> type();

    ITable<T> clone();
}
