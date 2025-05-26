package com.erp.erp.domain.cache.repository;

import com.erp.erp.domain.customer.common.dto.GetCustomerCache;
import com.erp.erp.global.util.ConverterUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisCacheRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  public void save(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Optional<GetCustomerCache> getGetCustomerCache(String key) {
    Object value = redisTemplate.opsForValue().get(key);
    GetCustomerCache cache = ConverterUtil.toObject(value, GetCustomerCache.class);
    return Optional.ofNullable(cache);
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }


}
