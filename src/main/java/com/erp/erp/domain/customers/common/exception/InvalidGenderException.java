package com.erp.erp.domain.customers.common.exception;

import lombok.Getter;

@Getter
public class InvalidGenderException extends RuntimeException{

  private final String code;
  public InvalidGenderException() {
    this(CustomersErrorType.Invalid_Gender.getMessage());
  }

  public InvalidGenderException(final String message) {
    super(message);
    this.code = CustomersErrorType.Invalid_Gender.name();
  }
}
