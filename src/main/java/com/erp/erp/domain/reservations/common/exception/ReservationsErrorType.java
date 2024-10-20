package com.erp.erp.domain.reservations.common.exception;

import lombok.Getter;

@Getter
public enum ReservationsErrorType {

  NOT_FOUND_RESERVATIONS("예약을 찾을 수 없습니다."),
  NO_AVAILABLE_SPOTS("예약 가능한 자리가 없습니다.");

  private final String message;

  ReservationsErrorType(String message) {
    this.message = message;
  }
}
