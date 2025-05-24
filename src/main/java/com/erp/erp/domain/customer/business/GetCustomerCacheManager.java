package com.erp.erp.domain.customer.business;


import com.erp.erp.domain.cache.common.enums.UpdateCacheStatus;
import com.erp.erp.domain.cache.common.mapper.CacheMapper;
import com.erp.erp.domain.cache.common.script.RedisScripts;
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
  private final CacheMapper cacheMapper;


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


  public void updateCacheWithTime(Long instituteId, List<GetCustomerDto.Response> response, LocalDateTime date) {

    String key = getKey(instituteId);

    long epochSecond = date.toEpochSecond(ZoneOffset.UTC);
    GetCustomerCache cache = cacheMapper.toGetCustomerCache(response, epochSecond);

    String customersJson = ConverterUtil.toJson(cache);



    List<String> args = List.of(
        String.valueOf(epochSecond),
        customersJson
    );

    String result = redisScriptRepository.execute(RedisScripts.getUpdateCacheScript(), key, args);
    UpdateCacheStatus status = UpdateCacheStatus.valueOf(result);
  }

  public void deleteCache(Long instituteId, LocalDateTime date) {
    String key = getKey(instituteId);
    long epochSecond = date.toEpochSecond(ZoneOffset.UTC);
    GetCustomerCache cache = cacheMapper.toGetCustomerCache(new ArrayList<>(), epochSecond);

    redisCacheRepository.save(key, cache);
  }
}
