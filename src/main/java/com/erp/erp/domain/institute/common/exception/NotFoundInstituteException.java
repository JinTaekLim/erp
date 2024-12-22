package com.erp.erp.domain.institute.common.exception;

import com.erp.erp.domain.institute.common.exception.type.InstituteErrorType;
import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundInstituteException extends BusinessException {

  private final String code;
  public NotFoundInstituteException() {
    this(InstituteErrorType.NOT_FOUND_INSTITUTE.getMessage());
  }

  public NotFoundInstituteException(final String message) {
    super(message);
    this.code = InstituteErrorType.NOT_FOUND_INSTITUTE.name();
  }
}
