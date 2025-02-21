package com.erp.erp.domain.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

  public final RedisTemplate<String, String> redisTemplate;

  private final String TOKEN_PREFIX = "token:";
  private final TimeUnit EXPIRATION_UNIT = TimeUnit.MILLISECONDS;

  @Value("${jwt.refresh.exp}")
  private long EXPIRATION_TIME;

    public String save(String refreshToken, String accountId) {
      String key = TOKEN_PREFIX + refreshToken;
      redisTemplate.opsForValue().set(
          key,
          accountId,
          EXPIRATION_TIME,
          EXPIRATION_UNIT
      );
      return key;
    }

  public String findByRefreshToken(String refreshToken) {
    String key = TOKEN_PREFIX + refreshToken;
    return redisTemplate.opsForValue().get(key);
  }

  public void deleteKey(String refreshToken) {
      String key = TOKEN_PREFIX + refreshToken;
      redisTemplate.delete(key);
  }
}
