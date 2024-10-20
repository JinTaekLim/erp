package com.erp.erp.domain.reservations.common.exception;

import com.erp.erp.global.error.BusinessException;
import lombok.Getter;

@Getter
public class NoAvailableSpotsException extends BusinessException {

  private final String code;
  public NoAvailableSpotsException() {
    this(ReservationsErrorType.NO_AVAILABLE_SPOTS.getMessage());
  }

  public NoAvailableSpotsException(final String message) {
    super(message);
    this.code = ReservationsErrorType.NO_AVAILABLE_SPOTS.name();
  }
}
