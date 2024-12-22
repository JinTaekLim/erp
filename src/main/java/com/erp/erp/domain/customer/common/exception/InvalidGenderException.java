package com.erp.erp.domain.customer.common.exception;

import com.erp.erp.domain.customer.common.exception.type.CustomerErrorType;
import lombok.Getter;

@Getter
public class InvalidGenderException extends RuntimeException{

  private final String code;
  public InvalidGenderException() {
    this(CustomerErrorType.Invalid_Gender.getMessage());
  }

  public InvalidGenderException(final String message) {
    super(message);
    this.code = CustomerErrorType.Invalid_Gender.name();
  }
}
