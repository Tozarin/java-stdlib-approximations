package generated.jakarta.servlet.http;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import generated.org.springframework.boot.pinnedValues.RequestMultiValueMapImpl;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.mock.web.MockHttpServletRequest;
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

    public String getParameter(String name) {
        return PinnedValueStorage.getPinnedValue(PinnedValueSource.REQUEST_PARAM, name, String.class);
    }

    public String getHeader(String name) {
        return PinnedValueStorage.getPinnedValue(PinnedValueSource.REQUEST_HEADER, name, String.class);
    }

    public String getContextPath() {
        return PinnedValueStorage.getPinnedValue(PinnedValueSource.REQUEST_PATH, String.class);
    }
}
