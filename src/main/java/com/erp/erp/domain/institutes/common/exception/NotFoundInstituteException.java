package com.erp.erp.domain.institutes.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundInstituteException extends BusinessException {

  private final String code;
  public NotFoundInstituteException() {
    this(InstitutesErrorType.NOT_FOUND_INSTITUTE.getMessage());
  }

  public NotFoundInstituteException(final String message) {
    super(message);
    this.code = InstitutesErrorType.NOT_FOUND_INSTITUTE.name();
  }
}
