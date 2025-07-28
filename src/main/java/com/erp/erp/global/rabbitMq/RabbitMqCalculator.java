package com.erp.erp.global.rabbitMq;

import java.time.LocalDateTime;

public class RabbitMqCalculator {

  // 현재 시각과 비교해, 주어진 시간으로 부터 N분 이내인지 확인
  public boolean isWithinNMinutes(LocalDateTime time, long n) {
    LocalDateTime now = LocalDateTime.now();
    if (time == null) return false;
    return !now.isAfter(time.plusMinutes(n));
  }
}
