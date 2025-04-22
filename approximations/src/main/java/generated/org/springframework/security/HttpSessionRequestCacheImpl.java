package generated.org.springframework.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Approximate(HttpSessionRequestCache.class)
public class HttpSessionRequestCacheImpl {
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        // No need to save session, we research only one request
    }
}
