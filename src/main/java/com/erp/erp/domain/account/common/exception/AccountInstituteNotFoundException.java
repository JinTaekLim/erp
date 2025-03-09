package com.erp.erp.domain.account.common.exception;

import com.erp.erp.domain.account.common.exception.type.AccountErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class AccountInstituteNotFoundException extends BusinessException {

  private final String code;
  public AccountInstituteNotFoundException() {
    this(AccountErrorType.ACCOUNT_INSTITUTE_NOT_FOUND.getMessage());
  }

  public AccountInstituteNotFoundException(final String message) {
    super(message);
    this.code = AccountErrorType.ACCOUNT_INSTITUTE_NOT_FOUND.name();
  }
}
