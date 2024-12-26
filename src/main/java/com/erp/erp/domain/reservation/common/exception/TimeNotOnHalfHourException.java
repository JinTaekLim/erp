package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class TimeNotOnHalfHourException extends BusinessException {

  private final String code;
  public TimeNotOnHalfHourException() {
    this(ReservationErrorType.TIME_NOT_ON_HALF_HOUR.getMessage());
  }

  public TimeNotOnHalfHourException(final String message) {
    super(message);
    this.code = ReservationErrorType.TIME_NOT_ON_HALF_HOUR.name();
  }
}
