package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import lombok.Getter;

import java.io.Serial;

@Getter
public class InvalidTokenException extends UnAuthenticatedException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String code;

  public InvalidTokenException() {
    super(TokenErrorType.INVALID_TOKEN.getMessage());
    this.code = TokenErrorType.INVALID_TOKEN.name();
  }

  public InvalidTokenException(final String message) {
    super(message);
    this.code = TokenErrorType.INVALID_TOKEN.name();
  }
}
