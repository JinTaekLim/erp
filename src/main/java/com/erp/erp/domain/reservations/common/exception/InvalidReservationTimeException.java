package com.erp.erp.domain.reservations.common.exception;

import com.erp.erp.global.error.BusinessException;
import lombok.Getter;

@Getter
public class InvalidReservationTimeException extends BusinessException {

  private final String code;
  public InvalidReservationTimeException() {
    this(ReservationsErrorType.INVALI_RESERVATION_TIME.getMessage());
  }

  public InvalidReservationTimeException(final String message) {
    super(message);
    this.code = ReservationsErrorType.INVALI_RESERVATION_TIME.name();
  }
}
