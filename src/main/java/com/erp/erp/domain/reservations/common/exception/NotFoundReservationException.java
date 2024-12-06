package com.erp.erp.domain.reservations.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundReservationException extends BusinessException {

  private final String code;
  public NotFoundReservationException() {
    this(ReservationsErrorType.NOT_FOUND_RESERVATIONS.getMessage());
  }

  public NotFoundReservationException(final String message) {
    super(message);
    this.code = ReservationsErrorType.NOT_FOUND_RESERVATIONS.name();
  }
}
