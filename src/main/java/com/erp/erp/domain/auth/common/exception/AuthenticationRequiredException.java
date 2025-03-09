package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import java.io.Serial;
import lombok.Getter;

@Getter
public class AuthenticationRequiredException extends UnAuthenticatedException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String code;

  public AuthenticationRequiredException() {
    super(TokenErrorType.AUTHENTICATION_REQUIRED.getMessage());
    this.code = TokenErrorType.INVALID_TOKEN.name();
  }

  public AuthenticationRequiredException(final String message) {
    super(message);
    this.code = TokenErrorType.AUTHENTICATION_REQUIRED.name();
  }
}
