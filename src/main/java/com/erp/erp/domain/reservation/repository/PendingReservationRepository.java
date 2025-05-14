package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PendingReservationRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private final String PENDING_RESERVATION_PREFIX = "pendingReservation:";

  private String getKey(Long instituteId) {
    return PENDING_RESERVATION_PREFIX + instituteId;
  }

  public void save(Long instituteId, PendingReservationDto dto) {
    String key = getKey(instituteId);
    redisTemplate.opsForList().leftPush(key, dto);
    redisTemplate.expire(key, 3, TimeUnit.HOURS);
  }

  public List<PendingReservationDto> findByInstituteId(Long instituteId) {
    String key = getKey(instituteId);
    List<Object> objects = redisTemplate.opsForList().range(key, 0,  - 1);

    if (objects == null) return new ArrayList<>();

    return objects.stream().map(o ->
        (PendingReservationDto) o
    ).toList();
  }

  public void delete(Long instituteId, PendingReservationDto dto) {
    String key = getKey(instituteId);
    redisTemplate.opsForList().remove(key, 1, dto);
  }

}
