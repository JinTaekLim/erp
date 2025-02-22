package com.erp.erp.global.error.exception;

import com.erp.erp.global.error.exception.type.ApiErrorType;
import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;

@Getter
public class ForbiddenException extends AccessDeniedException {

  private final String code;

  public ForbiddenException() {
    this(ApiErrorType.FORBIDDEN.getMessage());
  }

  public ForbiddenException(final String message) {
    this(message, ApiErrorType.FORBIDDEN.name());
  }

  public ForbiddenException(final String message, final String code) {
    super(message);
    this.code = code;
  }
}
