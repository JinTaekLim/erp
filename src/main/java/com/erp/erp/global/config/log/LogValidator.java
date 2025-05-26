package com.erp.erp.global.config.log;

import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class LogValidator {

  private final String[] IGNORE_URI = new String[]{
      "/manage/swagger-ui/**",
      "/manage/api-doc/**"
  };
  private final AntPathMatcher pathMatcher = new AntPathMatcher();


  public boolean isIgnoredUri(String uri) {
    return Arrays.stream(IGNORE_URI)
        .anyMatch(pattern -> pathMatcher.match(pattern, uri));  }
}
