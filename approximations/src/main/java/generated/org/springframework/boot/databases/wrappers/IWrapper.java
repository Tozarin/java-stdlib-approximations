package generated.org.springframework.boot.databases.wrappers;

import generated.org.springframework.boot.databases.ITable;

public interface IWrapper<T> {

    ITable<T> unwrap();
}
