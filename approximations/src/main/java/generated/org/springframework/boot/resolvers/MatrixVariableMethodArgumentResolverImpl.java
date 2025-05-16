package generated.org.springframework.boot.resolvers;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver;
import org.springframework.web.util.UrlPathHelper;

import java.util.HashMap;

@Approximate(MatrixVariableMethodArgumentResolver.class)
public class MatrixVariableMethodArgumentResolverImpl {
    @Nullable
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        if (!ResolverUtils.supportsMatrix(request))
            return null;
        return ResolverUtils.getNonEmptySymbolicString(PinnedValueSource.REQUEST_MATRIX, name);
    }
}
