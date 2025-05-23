package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.cache.repository.RedisCacheRepository;
import com.erp.erp.domain.cache.repository.RedisScriptRepository;
import com.erp.erp.domain.customer.common.dto.GetCustomerCache;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.global.util.ConverterUtil;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCustomerCacheManager {

  private final RedisCacheRepository<GetCustomerCache> redisCacheRepository;
  private final RedisScriptRepository redisScriptRepository;


  private final static String PREFIX = "get_customers_cache:";
  private String getKey(Long instituteId) {
    return PREFIX + instituteId;
  }

  public List<GetCustomerDto.Response> findByInstituteId(Long instituteId) {
    String key = getKey(instituteId);
    return Optional.ofNullable(redisCacheRepository.get(key))
        .map(GetCustomerCache::getGetCustomers)
        .orElseGet(ArrayList::new);
  }


  private static final String SCRIPT = """
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

  public String updateCacheWithTime(Long instituteId, List<GetCustomerDto.Response> response, LocalDateTime date) {

    String key = getKey(instituteId);

    long epochSecond = date.toEpochSecond(ZoneOffset.UTC);
    GetCustomerCache cache = getCustomerCache(response, epochSecond);

    String customersJson = ConverterUtil.toJson(cache);



    List<String> args = List.of(
        String.valueOf(epochSecond),
        customersJson
    );

    return redisScriptRepository.execute(SCRIPT, key, args);
  }

  private GetCustomerCache getCustomerCache(List<GetCustomerDto.Response> response, long date) {
    return GetCustomerCache.builder()
        .getCustomers(response)
        .updatedTime(date)
        .build();
  }

}
