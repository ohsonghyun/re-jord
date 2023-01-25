package com.dev6.rejordbe.presentation.controller.argumentResolver;

import com.dev6.rejordbe.domain.cookie.CookieNames;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * LoggedInUserArgumentResolver
 */
@Slf4j
public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoggedInParameter = parameter.hasParameterAnnotation(LoggedIn.class);
        boolean hasUid = String.class.isAssignableFrom(parameter.getParameterType());
        return hasLoggedInParameter && hasUid;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception
    {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalAccessException(ExceptionCode.ILLEGAL_ACCESS.name());
        }
        return session.getAttribute(CookieNames.LOGGED_IN_UID);
    }
}
