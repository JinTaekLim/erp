package com.erp.erp.domain.customer.common.exception;

import com.erp.erp.domain.customer.common.exception.type.CustomerErrorType;
import lombok.Getter;

@Getter
public class InvalidGenderException extends RuntimeException{

  private final String code;
  public InvalidGenderException() {
    this(CustomerErrorType.INVALID_GENDER.getMessage());
  }

  public InvalidGenderException(final String message) {
    super(message);
    this.code = CustomerErrorType.INVALID_GENDER.name();
  }
}
