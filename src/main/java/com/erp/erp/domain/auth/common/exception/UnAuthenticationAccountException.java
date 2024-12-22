package com.erp.erp.domain.auth.common.exception;

import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.ServerException;
import lombok.Getter;

@Getter
public class UnAuthenticationAccountException extends ServerException {

  private final String code;

  public UnAuthenticationAccountException() {
    this(TokenErrorType.UN_AUTHENTICATION_ACCOUNT_EXCEPTION.getMessage());
  }

  public UnAuthenticationAccountException(String message) {
    super(message);
    this.code = TokenErrorType.UN_AUTHENTICATION_ACCOUNT_EXCEPTION.name();
  }
}
