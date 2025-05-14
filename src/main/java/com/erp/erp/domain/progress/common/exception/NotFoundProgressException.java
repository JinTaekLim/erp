package com.erp.erp.domain.progress.common.exception;

import com.erp.erp.domain.customer.common.exception.type.CustomerErrorType;
import com.erp.erp.domain.progress.common.exception.type.ProgressErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundProgressException extends BusinessException {

  private final String code;
  public NotFoundProgressException() {
    this(ProgressErrorType.NOT_FOUND_PROGRESS.getMessage());
  }

  public NotFoundProgressException(final String message) {
    super(message);
    this.code = ProgressErrorType.NOT_FOUND_PROGRESS.name();
  }
}
