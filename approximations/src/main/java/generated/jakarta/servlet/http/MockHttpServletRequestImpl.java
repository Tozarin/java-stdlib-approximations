package generated.jakarta.servlet.http;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import generated.org.springframework.boot.pinnedValues.RequestMultiValueMapImpl;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.usvm.api.Engine;
import stub.java.util.map.RequestMap;
import stub.java.util.map.RequestMultiValueMap;

import java.util.*;

import static generated.org.springframework.boot.pinnedValues.PinnedValueStorage.getPinnedValue;

@Approximate(MockHttpServletRequest.class)
public class MockHttpServletRequestImpl {
    public Map<String, String[]> getParameterMap() { return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_PARAM); }
    public Map<String, String[]> _getHeaderMap() { return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_HEADER); }
    public Map<String, String[]> _matrixMap() { return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_MATRIX); }
    public Map<String, String[]> _pathMap() { return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_PATH); }

    private String possibleInput(PinnedValueSource source, String name) {
        String result = PinnedValueStorage.getPinnedValue(source, name, String.class);
        if (result == null)
            return null;

        Engine.assume(!result.isEmpty());
        return result;
    }

    public String getParameter(String name) {
        return possibleInput(PinnedValueSource.REQUEST_PARAM, name);
    }

    public String getHeader(String name) {
        return possibleInput(PinnedValueSource.REQUEST_HEADER, name);
    }

    public String getContextPath() {
        return PinnedValueStorage.getPinnedValue(PinnedValueSource.REQUEST_PATH, String.class);
    }
}
