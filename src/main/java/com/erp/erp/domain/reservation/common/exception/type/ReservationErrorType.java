package com.erp.erp.domain.reservation.common.exception.type;

import lombok.Getter;

@Getter
public enum ReservationErrorType {
  INVALID_RESERVATION_TIME("시작 시간은 종료 시간과 동일하거나 작을 수 없습니다."),
  NOT_FOUND_RESERVATION("예약을 찾을 수 없습니다."),
  NO_AVAILABLE_SEAT("예약 가능한 자리가 없습니다."),
  TIME_NOT_ON_HALF_HOUR("예약 시간은 30분 단위로만 입력 가능합니다."),
  INVALID_SEAT_RANGE("매장 내 좌석 번호 범위를 벗어났습니다.");

  private final String message;

  ReservationErrorType(String message) {
    this.message = message;
  }
}
