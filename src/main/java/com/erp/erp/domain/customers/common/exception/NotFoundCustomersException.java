package com.erp.erp.domain.customers.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundCustomersException extends BusinessException {

  private final String code;
  public NotFoundCustomersException() {
    this(CustomersErrorType.NOT_FOUND_CUSTOMERS.getMessage());
  }

  public NotFoundCustomersException(final String message) {
    super(message);
    this.code = CustomersErrorType.NOT_FOUND_CUSTOMERS.name();
  }
}
