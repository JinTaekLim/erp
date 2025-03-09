package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundReservationException extends BusinessException {

  private final String code;
  public NotFoundReservationException() {
    this(ReservationErrorType.NOT_FOUND_RESERVATION.getMessage());
  }

  public NotFoundReservationException(final String message) {
    super(message);
    this.code = ReservationErrorType.NOT_FOUND_RESERVATION.name();
  }
}
