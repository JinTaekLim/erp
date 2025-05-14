package com.erp.erp.global.rabbitMq;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {
  private List<QueueConfig> queues;

  @Getter
  @Setter
  public static class QueueConfig {
    private String exchange;
    private List<String> name;
  }
}
