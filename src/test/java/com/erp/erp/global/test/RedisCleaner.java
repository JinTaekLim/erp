package com.erp.erp.global.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCleaner {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  public void clear() {
    redisTemplate.getConnectionFactory()
        .getConnection()
        .serverCommands()
        .flushAll();
  }
}
