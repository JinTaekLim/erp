package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class InvalidSeatRangeException extends BusinessException {

  private final String code;
  public InvalidSeatRangeException() {
    this(ReservationErrorType.INVALID_SEAT_RANGE.getMessage());
  }

  public InvalidSeatRangeException(final String message) {
    super(message);
    this.code = ReservationErrorType.INVALID_SEAT_RANGE.name();
  }
}
