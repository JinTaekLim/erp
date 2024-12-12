package com.erp.erp.domain.accounts.common.exception;

import com.erp.erp.domain.accounts.common.exception.type.AccountErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import java.io.Serial;
import lombok.Getter;

@Getter
public class InvalidCredentialsException extends UnAuthenticatedException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String code;

  public InvalidCredentialsException() {
    super(AccountErrorType.INVALID_CREDENTIALS.getMessage());
    this.code = AccountErrorType.INVALID_CREDENTIALS.name();
  }

  public InvalidCredentialsException(final String message) {
    super(message);
    this.code = AccountErrorType.INVALID_CREDENTIALS.name();
  }
}
