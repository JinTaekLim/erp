package com.erp.erp.domain.auth.interceptor;

import com.erp.erp.domain.auth.business.TokenExtractor;
import com.erp.erp.domain.auth.common.exception.AuthenticationRequiredException;
import com.erp.erp.global.annotation.authentication.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final TokenExtractor tokenExtractor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    if (isViewRequest(handler) || swagger(request)) {return true; }
    if (hasAnnotation(handler, PermitAll.class)) return true;

    String accessToken = tokenExtractor.getTokenFromAuthorizationHeader(request);

    validateTokenPresence(accessToken);
    setAuthentication(accessToken);
    return true;
  }

  private boolean hasAnnotation(Object handler, Class<? extends Annotation> clazz) {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    return handlerMethod.getMethod().isAnnotationPresent(clazz);
  }

  private void validateTokenPresence(String token) {
    if(!StringUtils.hasText(token)) throw new AuthenticationRequiredException();
  }

  private void setAuthentication(String token) {
    Authentication authentication = tokenExtractor.getAuthenticationByAccessToken(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  // 정적 리소스 통과
  private boolean isViewRequest(Object handler) {
    return handler instanceof ResourceHttpRequestHandler;
  }

  // Swagger 관련 요청 통과
  public boolean swagger(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    return requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/api/swagger");
  }
}
