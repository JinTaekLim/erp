package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoAvailableSeatException extends BusinessException {

  private final String code;
  public NoAvailableSeatException() {
    this(ReservationErrorType.NO_AVAILABLE_SEAT.getMessage());
  }

  public NoAvailableSeatException(final String message) {
    super(message);
    this.code = ReservationErrorType.NO_AVAILABLE_SEAT.name();
  }
}
