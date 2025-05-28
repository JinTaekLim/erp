package com.erp.erp.global.rabbitMq.connect;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqAuthProvider {

  private static final String BASIC_PREFIX = "Basic ";

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  public String getAuthHeader() {
    String credentials = username + ":" + password;
    return BASIC_PREFIX + Base64.getEncoder().encodeToString(
        credentials.getBytes(StandardCharsets.UTF_8)
    );
  }

}
