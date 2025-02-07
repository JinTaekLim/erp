package com.erp.erp.domain.admin.common.exception;

import com.erp.erp.domain.admin.common.exception.type.AdminErrorType;
import com.erp.erp.global.error.exception.UnAuthenticatedException;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends UnAuthenticatedException {

  private final String code;
  public UnauthorizedAccessException() {
    this(AdminErrorType.UNAUTHORIZED_ACCESS.getMessage());
  }

  public UnauthorizedAccessException(final String message) {
    super(message);
    this.code = AdminErrorType.UNAUTHORIZED_ACCESS.name();
  }
}
