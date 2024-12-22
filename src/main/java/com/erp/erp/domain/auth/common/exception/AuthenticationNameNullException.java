package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.ServerException;
import lombok.Getter;

@Getter
public class AuthenticationNameNullException extends ServerException {

  private final String code;

  public AuthenticationNameNullException() {
    this(TokenErrorType.AUTHENTICATION_NAME_NULL_ERROR.getMessage());
  }

  public AuthenticationNameNullException(String message) {
    super(message);
    this.code = TokenErrorType.AUTHENTICATION_NAME_NULL_ERROR.name();
  }
}
