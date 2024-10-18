package com.erp.erp.domain.accounts.common.exception;

import lombok.Getter;

@Getter
public class InvalidGenderException extends RuntimeException{

  private final String code;
  public InvalidGenderException() {
    this(AccountsErrorType.Invalid_Gender.getMessage());
  }

  public InvalidGenderException(final String message) {
    super(message);
    this.code = AccountsErrorType.Invalid_Gender.name();
  }
}
