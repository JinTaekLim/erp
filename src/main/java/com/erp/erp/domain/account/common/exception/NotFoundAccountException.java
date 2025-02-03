package com.erp.erp.domain.account.common.exception;

import com.erp.erp.domain.account.common.exception.type.AccountErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundAccountException extends BusinessException {

  private final String code;
  public NotFoundAccountException() {
    this(AccountErrorType.NOT_FOUND_ACCOUNT.getMessage());
  }

  public NotFoundAccountException(final String message) {
    super(message);
    this.code = AccountErrorType.NOT_FOUND_ACCOUNT.name();
  }
}
