package com.erp.erp.domain.plans.common.exception;

import com.erp.erp.global.error.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundPlansException extends BusinessException {

  private final String code;
  public NotFoundPlansException() {
    this(PlansErrorType.NOT_FOUND_PLANS.getMessage());
  }

  public NotFoundPlansException(final String message) {
    super(message);
    this.code = PlansErrorType.NOT_FOUND_PLANS.name();
  }
}
