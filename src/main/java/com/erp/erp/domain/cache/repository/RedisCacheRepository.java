package com.erp.erp.domain.cache.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisCacheRepository<T> {

  private final RedisTemplate<String, T> redisTemplate;

  public void save(String key, T value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public T get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }


}
