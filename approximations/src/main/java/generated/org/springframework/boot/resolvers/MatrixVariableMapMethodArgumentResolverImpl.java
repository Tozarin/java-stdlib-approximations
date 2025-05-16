package generated.org.springframework.boot.resolvers;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.RequestMapImpl;
import generated.org.springframework.boot.pinnedValues.RequestMultiValueMapImpl;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver;
import org.springframework.web.util.UrlPathHelper;
import stub.java.util.map.RequestMap;
import stub.java.util.map.RequestMultiValueMap;

import java.util.HashMap;
import java.util.List;

@Approximate(MatrixVariableMapMethodArgumentResolver.class)
public class MatrixVariableMapMethodArgumentResolverImpl {
    @Nullable
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest request, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        boolean supportsMatrix = ResolverUtils.supportsMatrix(request);

        if (isSingleValueMap(parameter)) {
            if (!supportsMatrix) return new HashMap<String, String[]>();
            return new RequestMapImpl(PinnedValueSource.REQUEST_MATRIX, false);
        }

        if (!supportsMatrix) return new HashMap<String, String>();
        return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_MATRIX);
    }

    private boolean isSingleValueMap(MethodParameter parameter) {
        if (!MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
            ResolvableType[] genericTypes = ResolvableType.forMethodParameter(parameter).getGenerics();
            if (genericTypes.length == 2) {
                return !List.class.isAssignableFrom(genericTypes[1].toClass());
            }
        }
        return false;
    }
}
