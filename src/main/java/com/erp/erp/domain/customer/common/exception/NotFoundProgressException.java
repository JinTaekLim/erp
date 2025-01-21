package com.erp.erp.domain.customer.common.exception;

import com.erp.erp.domain.customer.common.exception.type.CustomerErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundProgressException extends BusinessException {

  private final String code;
  public NotFoundProgressException() {
    this(CustomerErrorType.NOT_FOUND_PROGRESS.getMessage());
  }

  public NotFoundProgressException(final String message) {
    super(message);
    this.code = CustomerErrorType.NOT_FOUND_PROGRESS.name();
  }
}
