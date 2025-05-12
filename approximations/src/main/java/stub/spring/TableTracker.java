package stub.spring;

public class TableTracker {

    static <T> void track(String tableName, ListWrapper<T> values) {
        throw new LinkageError();
    }
}
