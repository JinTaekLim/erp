package com.erp.erp.domain.institutes.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class InstituteNotFoundInCustomerException extends BusinessException {

  private final String code;
  public InstituteNotFoundInCustomerException() {
    this(InstitutesErrorType.INSTITUTES_NOT_FOUND_IN_CUSTOMERS.getMessage());
  }

  public InstituteNotFoundInCustomerException(final String message) {
    super(message);
    this.code = InstitutesErrorType.INSTITUTES_NOT_FOUND_IN_CUSTOMERS.name();
  }
}
