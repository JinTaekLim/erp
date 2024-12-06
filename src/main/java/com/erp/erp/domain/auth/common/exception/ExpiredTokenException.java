package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ExpiredTokenException extends UnAuthenticatedException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String code;

  public ExpiredTokenException() {
    super(TokenErrorType.EXPIRED_TOKEN.getMessage());
    this.code = TokenErrorType.EXPIRED_TOKEN.name();
  }

  public ExpiredTokenException(final String message) {
    super(message);
    this.code = TokenErrorType.EXPIRED_TOKEN.name();
  }
}
