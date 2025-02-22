package com.erp.erp.global.config.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;


@Slf4j
public class LogFilter extends OncePerRequestFilter {

  private final UUID uuid = UUID.randomUUID();
  private final String UUID_KEY = "uuid";

  private final String SWAGGER_REQUEST = "swagger request";
  private final String[] SWAGGER_URL = { "/api/swagger-ui/swagger-initializer.js", "/api-docs/swagger-config",
          "/api/swagger-ui/favicon-32x32.png", "/api-docs", "/api/swagger-ui/index.html" };
  private boolean isSwagger = false;

  private long startTime;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    CachingRequestWrapper requestWrapper = new CachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    startTime = System.currentTimeMillis();

    try {
      MDC.put(UUID_KEY, uuid.toString());
      logRequest(requestWrapper);
      filterChain.doFilter(requestWrapper, responseWrapper);
    } finally {
      logResponse(responseWrapper);
      responseWrapper.copyBodyToResponse();
      MDC.clear();
    }
  }

  private void logRequest(HttpServletRequest request) throws IOException {
    String queryString = request.getQueryString();
    String uri = request.getRequestURI();
    String uriPlusQueryString = uri + "?" + queryString;
    isSwaggerRequest(uri);

    printRequest(
            request.getMethod(),
            queryString == null ? uri : uriPlusQueryString,
            request.getContentType(),
            getBody(request.getInputStream()),
            getClientIp(request)
    );
  }



  private void logResponse(ContentCachingResponseWrapper response)
      throws IOException {
    String body = getBody(response.getContentInputStream());
    if (isSwagger) { body = SWAGGER_REQUEST;}
    printResponse(response.getStatus(), body);
  }



  private void printRequest(String method, String url, String contentType, String body, String ip) {
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    log.info("Request : {} uri=[{}] content-type=[{}], body=[{}], client-ip=[{}]", method, url, contentType, body, ip);
  }

  private void printResponse(int status, String body) {
    log.info("Response : {} body=[{}]", status, body);
    log.info("Request processed in {}ms", (System.currentTimeMillis() - startTime));
    log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

  public String getBody(InputStream is) throws IOException {
    byte[] content = StreamUtils.copyToByteArray(is);
    if (content.length == 0) {
      return null;
    }
    return new String(content, StandardCharsets.UTF_8);
  }

  private String getClientIp(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");
    if (clientIp == null || clientIp.isEmpty()) {
      clientIp = request.getRemoteAddr();
    }
    return clientIp;
  }

  private void isSwaggerRequest(String uri) {
    isSwagger = Arrays.asList(SWAGGER_URL).contains(uri);
  }



}