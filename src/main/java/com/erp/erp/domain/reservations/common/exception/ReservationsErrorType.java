package com.erp.erp.domain.reservations.common.exception;

import lombok.Getter;

@Getter
public enum ReservationsErrorType {
  INVALI_RESERVATION_TIME("시작 시간은 종료 시간과 동일하거나 작을 수 없습니다."),
  NOT_FOUND_RESERVATIONS("예약을 찾을 수 없습니다."),
  NO_AVAILABLE_SPOTS("예약 가능한 자리가 없습니다.");

  private final String message;

  ReservationsErrorType(String message) {
    this.message = message;
  }
}
