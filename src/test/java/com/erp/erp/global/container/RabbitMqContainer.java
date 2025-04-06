package com.erp.erp.global.container;


import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class RabbitMqContainer {

  private static final int RABBITMQ_PORT = 5672;

  @Container
  private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.6-management")
      .withExposedPorts(RABBITMQ_PORT);

  static {
    rabbitMQContainer.start();
    System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
    System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getMappedPort(RABBITMQ_PORT).toString());
  }
}