package generated.org.springframework.http;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import stub.java.util.map.RequestMap;

import static generated.org.springframework.boot.pinnedValues.PinnedValueSource.REQUEST_HEADER;
import static generated.org.springframework.boot.pinnedValues.PinnedValueSource.REQUEST_MEDIA_TYPE_NAME;

@Approximate(HttpHeaders.class)
public class HttpHeadersImpl {
    public void set(String headerName, @Nullable String headerValue) {
        PinnedValueStorage.writePinnedValue(REQUEST_HEADER, headerName, headerValue);
    }

    @Nullable
    public MediaType getContentType() {
        // TODO: Make symbolic with pre-defined values that will be accepted by respective message converter
        MediaType mediaType = MediaType.APPLICATION_JSON;
        MediaTypeName mediaTypeName = MediaTypeName.APPLICATION_JSON;
        PinnedValueStorage.writePinnedValue(REQUEST_MEDIA_TYPE_NAME, mediaTypeName.name());
        return mediaType;
    }
}
