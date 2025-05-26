package com.erp.erp.global.config.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;


@Slf4j
@RequiredArgsConstructor
public class LogFilter extends OncePerRequestFilter {

  private final static String UUID_KEY = "uuid";
  private final LogManager logManager;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    CachingRequestWrapper requestWrapper = new CachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    // 시작 시간 기록
    long startTime = System.currentTimeMillis();
    String uri = requestWrapper.getRequestURI();

    try {
      MDC.put(UUID_KEY, UUID.randomUUID().toString());
      logManager.logRequest(requestWrapper);
      filterChain.doFilter(requestWrapper, responseWrapper);
    } finally {
      logManager.logResponse(responseWrapper, startTime, uri);
      responseWrapper.copyBodyToResponse();
      MDC.clear();
    }
  }



}