package com.erp.erp.domain.payments.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundPaymentsException extends BusinessException {

  private final String code;
  public NotFoundPaymentsException() {
    this(PaymentsErrorType.NOT_FOUND_PAYMENTS.getMessage());
  }

  public NotFoundPaymentsException(final String message) {
    super(message);
    this.code = PaymentsErrorType.NOT_FOUND_PAYMENTS.name();
  }
}
