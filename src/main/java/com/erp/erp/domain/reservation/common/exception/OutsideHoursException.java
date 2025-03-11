package com.erp.erp.domain.reservation.common.exception;

import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class OutsideHoursException extends BusinessException {

  private final String code;
  public OutsideHoursException() {
    this(ReservationErrorType.OUT_SIDE_HOURS.getMessage());
  }

  public OutsideHoursException(final String message) {
    super(message);
    this.code = ReservationErrorType.OUT_SIDE_HOURS.name();
  }
}
