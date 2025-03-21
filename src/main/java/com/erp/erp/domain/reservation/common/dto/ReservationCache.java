package com.erp.erp.domain.reservation.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationCache {

  @NotNull
  private Long instituteId;
  @NotNull
  private Long customerId;
  private Long reservationId;
  private double usedTime;
  private int lateCount;
  private int absenceCount;

  @Builder
  public ReservationCache(Long instituteId, Long customerId, Long reservationId, double usedTime,
      int lateCount, int absenceCount) {
    this.instituteId = instituteId;
    this.customerId = customerId;
    this.reservationId = reservationId;
    this.usedTime = usedTime;
    this.lateCount = lateCount;
    this.absenceCount = absenceCount;
  }

  public ReservationCache update(Long reservationId, double usedTime, int lateCount, int absenceCount) {
    this.reservationId = reservationId;
    this.usedTime = usedTime;
    this.lateCount = lateCount;
    this.absenceCount = absenceCount;
    return this;
  }
}
