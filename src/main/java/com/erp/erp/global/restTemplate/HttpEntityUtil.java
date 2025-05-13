package com.erp.erp.global.restTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpEntityUtil {

  public HttpEntity<String> getJosnHttpEntity(String message) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String payload = String.format("{\"content\": \"%s\"}", message);
    return new HttpEntity<>(payload, headers);
  }

}
