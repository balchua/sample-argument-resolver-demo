package com.github.balchua.argumenthandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Slf4j
public class UiProxyArgumentHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(UiProxy.class);
    }

    private MultiValueMap<String, String> convertParameter(Map<String, String[]> parameters) {
        MultiValueMap<String, String> multi = new LinkedMultiValueMap();
        parameters.forEach((k,v) -> {
            log.debug("Key: {}, Value: {}", k, v);
            multi.add(k, v[0]);
        });
        return multi;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest();
        MultiValueMap<String, String> params = convertParameter(request.getParameterMap());
        HttpHeaders headers = new HttpHeaders();
        nativeWebRequest.getHeaderNames().forEachRemaining(item -> {
            String value = nativeWebRequest.getHeader(item);
            headers.add(item, value);
        });
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UiProxy uiProxy = new UiProxy(entity,HttpMethod.valueOf(request.getMethod()), params);

        return uiProxy;

    }
}
