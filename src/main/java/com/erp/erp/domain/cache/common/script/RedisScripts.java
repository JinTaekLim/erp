package com.erp.erp.domain.cache.common.script;

public class RedisScripts {

  private static final String UPDATE_CACHE_SCRIPT = """
        local value = redis.call('GET', KEYS[1])
      
        if value == false then
            redis.call('SET', KEYS[1], ARGV[2])
            return "SUCCESS"
        end
      
        local json = cjson.decode(value)
        local time = tonumber(json.updatedTime)
        local updatedTime = tonumber(ARGV[1])
      
        if time == nil or time <= updatedTime then
            redis.call('SET', KEYS[1], ARGV[2])
            return "UPDATE"
        else
            return "SKIP"
        end
      
      """;

  public static String getUpdateCacheScript() {
    return UPDATE_CACHE_SCRIPT;
  }

}
