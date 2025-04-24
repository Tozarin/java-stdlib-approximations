package generated.org.springframework.boot.resolvers;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.PinnedValueStorage;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import static generated.org.springframework.boot.SpringApplicationImpl._println;

@Approximate(PathVariableMethodArgumentResolver.class)
public class PathVariableMethodArgumentResolverImpl {
    @Nullable
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String parameterName = parameter.getParameterName();
        String path = request.getContextPath();
        if (parameterName == null)
            return null;

        if (!path.contains(String.format("{%s}", parameterName)))
            return null;

        return ResolverUtils.getNonEmptySymbolicString(PinnedValueSource.REQUEST_PATH_VARIABLE, name);
    }
}
