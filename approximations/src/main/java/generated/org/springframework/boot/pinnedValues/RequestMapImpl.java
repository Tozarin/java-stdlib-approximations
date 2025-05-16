package generated.org.springframework.boot.pinnedValues;

import org.jacodb.approximation.annotation.Approximate;
import org.usvm.api.Engine;
import stub.java.util.map.RequestMap;

@Approximate(RequestMap.class)
public class RequestMapImpl extends RequestMap{
    private final PinnedValueSource source;
    private final boolean canBeEmpty;

    public RequestMapImpl(PinnedValueSource source, boolean canBeEmpty) {
        this.source = source;
        this.canBeEmpty = canBeEmpty;
    }

    public String get(Object key) {
        String result = PinnedValueStorage.getPinnedValue(source, (String)key, String.class);
        if (result == null)
            return null;
        if (!canBeEmpty)
            Engine.assume(!result.isEmpty());
        return result;
    }
}
