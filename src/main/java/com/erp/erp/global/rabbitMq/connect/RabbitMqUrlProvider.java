package com.erp.erp.global.rabbitMq.connect;

import org.springframework.stereotype.Component;

@Component
public class RabbitMqUrlProvider {

  private static String HTTP_URL_FORMAT = "http://%s";

  public String getUrl(String ip) {
    return String.format(HTTP_URL_FORMAT, ip);
  }
}
