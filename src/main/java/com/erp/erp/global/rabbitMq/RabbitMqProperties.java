package com.erp.erp.global.rabbitMq;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {

  private Map<String, QueueInfo> queues;


  public String getName(String key) {
    return queues.get(key).getName();
  }

  public String getExchange(String key) {
    return queues.get(key).getExchange();
  }

  @Getter
  @Setter
  public static class QueueInfo {
    private String name;
    private String exchange;
  }
}
