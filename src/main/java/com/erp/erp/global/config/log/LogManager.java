package com.erp.erp.global.config.log;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@RequiredArgsConstructor
public class LogManager {

  private final LogPrinter logPrinter;
  private final LogExtractor logExtractor;
  private final LogValidator logValidator;

  public void logRequest(HttpServletRequest request) throws IOException {
    logPrinter.printRequestLog(
        request.getMethod(),
        logExtractor.getUriPlusQueryString(request),
        request.getContentType(),
        logExtractor.getBody(request.getInputStream()),
        logExtractor.getClientIp(request)
    );
  }


  public void logResponse(ContentCachingResponseWrapper response, long startTime, String uri)
      throws IOException {
    String body = logExtractor.getBody(response.getContentInputStream());

    if (!logValidator.isIgnoredUri(uri)) {
      logPrinter.printResponseLog(response.getStatus(), body, startTime);
    }
  }

}
