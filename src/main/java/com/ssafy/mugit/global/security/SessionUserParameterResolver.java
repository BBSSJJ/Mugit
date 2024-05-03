package com.ssafy.mugit.global.security;

import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.DataKeys;
import com.ssafy.mugit.global.dto.UserSessionDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class SessionUserParameterResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isUserSessionAnnotation = parameter.getParameterAnnotation(UserSession.class) != null;
        boolean isUserSessionDtoClass = parameter.getParameterType().equals(UserSessionDto.class);
        return isUserSessionAnnotation && isUserSessionDtoClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute(DataKeys.LOGIN_USER_KEY.getKey());
    }
}
