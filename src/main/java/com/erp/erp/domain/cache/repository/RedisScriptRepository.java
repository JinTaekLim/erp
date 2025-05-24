package com.erp.erp.domain.cache.repository;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisScriptRepository {

  private final RedisTemplate<String, String> redisTemplate;

  private <T> DefaultRedisScript<T> getScript(String script, Class<T> clazz) {
    DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
    redisScript.setScriptText(script);
    redisScript.setResultType(clazz);
    return redisScript;
  }


  public String execute(String scriptText, String key, List<String> args) {
    DefaultRedisScript<String> redisScript = getScript(scriptText, String.class);
    return redisTemplate.execute(redisScript, Collections.singletonList(key), args.toArray());
  }
}
