package com.erp.erp.domain.auth.interceptor;

import com.erp.erp.domain.auth.business.TokenExtractor;
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

  private final TokenExtractor tokenExtractor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    // Swagger 같은 js/html 관련 파일들은 통과한다.(view 관련 요청 = ResourceHttpRequestHandler)
    if (handler instanceof ResourceHttpRequestHandler) { return true; }

    String accessToken = tokenExtractor.getTokenFromAuthorizationHeader(request);
    if (isValidToken(accessToken)) {setAuthentication(accessToken);}

    return true;
  }

  private boolean isValidToken(String token) {
    return token != null && !token.isEmpty();
  }

  private void setAuthentication(String token) {
    Authentication authentication = tokenExtractor.getAuthenticationByAccessToken(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
