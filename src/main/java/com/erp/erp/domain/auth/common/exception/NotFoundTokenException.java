package com.erp.erp.domain.auth.common.exception;


import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;

public class NotFoundTokenException extends UnAuthenticatedException {

  private final String code;

  public NotFoundTokenException() {
    super(TokenErrorType.NOT_FOUND_TOKEN.getMessage());
    this.code = TokenErrorType.NOT_FOUND_TOKEN.name();
  }

  public NotFoundTokenException(final String message) {
    super(message);
    this.code = TokenErrorType.NOT_FOUND_TOKEN.name();
  }
}
