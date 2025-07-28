package com.erp.erp.global.rabbitMq;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListenerCacheManager {

  private static String KEY = "LISTENER_CACHE";
  private final ConcurrentHashMap<String, LocalDateTime> updatedAt = new ConcurrentHashMap<>();

  public void updateAt() {
    updatedAt.put(KEY, LocalDateTime.now());
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt.get(KEY);
  }


}
