package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationCacheRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private final String INSTITUTE_PREFIX = "institute:";
  private final int MAX_SIZE = 19;

  private String getKey(Long instituteId) {
    return INSTITUTE_PREFIX + instituteId;
  }

  public void save(ReservationCache dto) {
    String key = getKey(dto.getInstituteId());

    redisTemplate.opsForList().leftPush(key, dto);
    redisTemplate.opsForList().trim(key, 0, MAX_SIZE);
  }

  public List<ReservationCache> findByInstituteId(Long instituteId) {
    String key = getKey(instituteId);
    List<Object> objects = redisTemplate.opsForList().range(key, 0, MAX_SIZE);

    if (objects == null) return Collections.emptyList();

    return objects.stream().map(o -> {
      return (ReservationCache) o;
    }).toList();
  }

  public Optional<ReservationCache> findByCustomerId(Long instituteId, Long customerId) {
    String key = getKey(instituteId);
    List<Object> objects = redisTemplate.opsForList().range(key, 0, MAX_SIZE);

    if (objects == null) return Optional.empty();

    return objects.stream()
        .map(o -> (ReservationCache) o)
        .filter(reservation -> reservation.getCustomerId().equals(customerId))
        .findFirst();
  }

  public void update(ReservationCache reservationCache) {
    String key = getKey(reservationCache.getInstituteId());

    List<Object> objects = redisTemplate.opsForList().range(key, 0, MAX_SIZE);
    if (objects == null || objects.isEmpty()) return;

    for (int i = 0; i < objects.size(); i++) {
      ReservationCache existing = (ReservationCache) objects.get(i);
      if (existing.getCustomerId().equals(reservationCache.getCustomerId())) {
        redisTemplate.opsForList().set(key, i, reservationCache);
        return;
      }
    }
  }

  public void update(Long instituteId, List<ReservationCache> reservationCacheList) {
    String key = getKey(instituteId);

    redisTemplate.delete(key);

    for (ReservationCache reservationCache : reservationCacheList) {
      redisTemplate.opsForList().leftPush(key, reservationCache);
    }
    redisTemplate.opsForList().trim(key, 0, MAX_SIZE);
  }

}
