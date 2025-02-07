package com.erp.erp.domain.admin.common.exception;

import com.erp.erp.domain.admin.common.exception.type.AdminErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundAdminException extends BusinessException {

  private final String code;
  public NotFoundAdminException() {
    this(AdminErrorType.NOT_FOUND_ADMIN.getMessage());
  }

  public NotFoundAdminException(final String message) {
    super(message);
    this.code = AdminErrorType.NOT_FOUND_ADMIN.name();
  }
}
