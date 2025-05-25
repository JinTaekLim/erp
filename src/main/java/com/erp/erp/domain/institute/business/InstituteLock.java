package com.erp.erp.domain.institute.business;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstituteLock {

  private final RedissonClient redissonClient;

  private final String INSTITUTE_LOCK_PREFIX = "instituteLock:";

  private String getKey(Long instituteId) {
    return INSTITUTE_LOCK_PREFIX  + instituteId;
  }

  public boolean getLock(Long instituteId) throws InterruptedException {
    String key = getKey(instituteId);
    RLock rLock = redissonClient.getLock(key);
    return rLock.tryLock(10, TimeUnit.SECONDS);
  }

  public void unLock(Long instituteId) {
    String key = getKey(instituteId);
    RLock rLock = redissonClient.getLock(key);
    rLock.unlock();
  }

  public void executeWithLock(Long instituteId, Runnable action) {
    try {
      getLock(instituteId);
      action.run();
    } catch (InterruptedException e) {
      log.error(e.getMessage());
      throw new RuntimeException("락 획득 실패");
    } finally {
      unLock(instituteId);
    }
  }

}
