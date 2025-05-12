package generated.org.springframework.boot.databases.wrappers;

import java.util.List;

public interface IListWrapper<T> extends List<T>, IWrapper<T> {

    int getSizeOfCache();

    int getModCount();

    int getWrpStartIx();

    int getWrpEndIx();

    T getFromCache(int ix);

    T cacheNext();

    void cacheUntilIx(int ix);
}
