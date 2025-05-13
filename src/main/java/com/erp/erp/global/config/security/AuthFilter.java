package com.erp.erp.global.config.security;

import com.erp.erp.domain.auth.business.TokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

  private final TokenExtractor tokenExtractor;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String token = tokenExtractor.getTokenFromAuthorizationHeader(request);

    if (token != null) {
      Authentication authentication = tokenExtractor.getAuthenticationByAccessToken(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);

  }
}
