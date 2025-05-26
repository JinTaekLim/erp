package com.erp.erp.global.config.log;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
@RequiredArgsConstructor
public class LogExtractor {

  public String getUriPlusQueryString(HttpServletRequest request) {
    String queryString = request.getQueryString();
    String uri = request.getRequestURI();
    String uriPlustQueryString = uri + "?" + queryString;

    return (queryString == null) ? uri : uriPlustQueryString;
  }

  public String getBody(InputStream is) throws IOException {
    byte[] content = StreamUtils.copyToByteArray(is);

    if (content.length == 0) {return null;}

    return new String(content, StandardCharsets.UTF_8);
  }

  public String getClientIp(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");

    if (clientIp == null || clientIp.isEmpty()) {return request.getRemoteAddr();}

    return clientIp;
  }
}
