package generated.org.springframework.security;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jacodb.approximation.annotation.Approximate;

import static generated.org.springframework.boot.pinnedValues.PinnedValueStorage.getPinnedValue;

@Approximate(HttpServletRequestWrapper.class)
public class HttpServletRequestWrapperImpl {

    public String getHeader(String name) {
        return getPinnedValue(PinnedValueSource.REQUEST_HEADER, name, String.class);
    }
}
