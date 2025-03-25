package com.erp.erp.global.container;


import java.time.Duration;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class RedisContainer {

  private static final int REDIS_PORT = 6379;

  @Container
  private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.4")
      .withExposedPorts(REDIS_PORT)
      .waitingFor(Wait.forListeningPort())
      .withStartupTimeout(Duration.ofSeconds(60));

  static {
    redisContainer.start();
    System.setProperty("spring.data.redis.host", redisContainer.getHost());
    System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(REDIS_PORT).toString());
  }
}