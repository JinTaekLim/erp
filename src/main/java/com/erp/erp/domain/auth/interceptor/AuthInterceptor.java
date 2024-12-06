package com.erp.erp.domain.auth.interceptor;

import com.erp.erp.domain.auth.business.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final TokenProvider tokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    // Swagger 같은 js/html 관련 파일들은 통과한다.(view 관련 요청 = ResourceHttpRequestHandler)
    if (handler instanceof ResourceHttpRequestHandler) { return true; }

    String accessToken = tokenProvider.getTokenFromAuthorizationHeader(request);
    if (isValidToken(accessToken)) {setAuthentication(accessToken);}

    return true;
  }

  private boolean isValidToken(String token) {
    return token != null && !token.isEmpty();
  }

  private void setAuthentication(String token) {
    Authentication authentication = tokenProvider.getAuthenticationByAccessToken(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
