package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidReservationTimeException extends BusinessException {

  private final String code;
  public InvalidReservationTimeException() {
    this(ReservationErrorType.INVALID_RESERVATION_TIME.getMessage());
  }

  public InvalidReservationTimeException(final String message) {
    super(message);
    this.code = ReservationErrorType.INVALID_RESERVATION_TIME.name();
  }
}
