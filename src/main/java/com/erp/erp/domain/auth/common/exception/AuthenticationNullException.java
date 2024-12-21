package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.ServerException;
import lombok.Getter;

@Getter
public class AuthenticationNullException extends ServerException {

  private final String code;

  public AuthenticationNullException() {
    this(TokenErrorType.AUTHENTICATION_NULL_ERROR.getMessage());
  }

  public AuthenticationNullException(String message) {
    super(message);
    this.code = TokenErrorType.AUTHENTICATION_NULL_ERROR.name();
  }
}
