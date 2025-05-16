package generated.org.springframework.boot.resolvers;

import generated.org.springframework.boot.pinnedValues.PinnedValueSource;
import generated.org.springframework.boot.pinnedValues.RequestMapImpl;
import generated.org.springframework.boot.pinnedValues.RequestMultiValueMapImpl;
import org.jacodb.approximation.annotation.Approximate;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import stub.java.util.map.RequestMap;
import stub.java.util.map.RequestMultiValueMap;

@Approximate(RequestParamMapMethodArgumentResolver.class)
public class RequestParamMapMethodArgumentResolverImpl {
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

        if (MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
            return new RequestMultiValueMapImpl(PinnedValueSource.REQUEST_PARAM);
        }

        return new RequestMapImpl(PinnedValueSource.REQUEST_PARAM, false);
    }
}
