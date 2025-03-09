package com.erp.erp.global.error.exception;

import com.erp.erp.global.error.exception.type.ApiErrorType;
import lombok.Getter;

@Getter
public class UnAuthenticatedException extends RuntimeException {

  private final String code;

  public UnAuthenticatedException() {
    this(ApiErrorType.UNAUTHENTICATED.getMessage());
  }

  public UnAuthenticatedException(final String message) {
    this(message, ApiErrorType.UNAUTHENTICATED.name());
  }

  public UnAuthenticatedException(final String message, final String code) {
    super(message);
    this.code = code;
  }
}
