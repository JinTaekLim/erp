package com.erp.erp.global.restTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {

  private final RestTemplate restTemplate = new RestTemplate();
  private final HttpEntityUtil httpEntityUtil = new HttpEntityUtil();

  public void sendPost(String url, String message) {
    HttpEntity<String> entity = httpEntityUtil.getJosnHttpEntity(message);
    restTemplate.postForEntity(url, entity, String.class);
  }

}
