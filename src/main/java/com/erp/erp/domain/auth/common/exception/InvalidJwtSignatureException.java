package com.erp.erp.domain.auth.common.exception;


import com.erp.erp.domain.auth.common.exception.type.TokenErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import lombok.Getter;

@Getter
public class InvalidJwtSignatureException extends UnAuthenticatedException {

  private final String code;

  public InvalidJwtSignatureException() {
    super(TokenErrorType.INVALID_JWT_SIGNATURE.getMessage());
    this.code = TokenErrorType.INVALID_JWT_SIGNATURE.name();
  }

  public InvalidJwtSignatureException(final String message) {
    super(message);
    this.code = TokenErrorType.INVALID_JWT_SIGNATURE.name();
  }
}
