package generated.org.springframework.boot.pinnedValues;

import org.jacodb.approximation.annotation.Approximate;
import stub.java.util.map.RequestMap;

@Approximate(RequestMap.class)
public class RequestMapImpl extends RequestMap{
    private final PinnedValueSource source;

    public RequestMapImpl(PinnedValueSource source) {
        this.source = source;
    }

    public String get(Object key) {
        return PinnedValueStorage.getPinnedValue(source, (String)key, String.class);
    }
}
