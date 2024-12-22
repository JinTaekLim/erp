package com.erp.erp.domain.customer.common.exception;

import com.erp.erp.domain.customer.common.exception.type.CustomerErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundCustomerException extends BusinessException {

  private final String code;
  public NotFoundCustomerException() {
    this(CustomerErrorType.NOT_FOUND_CUSTOMERS.getMessage());
  }

  public NotFoundCustomerException(final String message) {
    super(message);
    this.code = CustomerErrorType.NOT_FOUND_CUSTOMERS.name();
  }
}
