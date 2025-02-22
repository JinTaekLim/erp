package com.erp.erp.domain.plan.common.exception;

import com.erp.erp.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NotFoundPlanException extends BusinessException {

  private final String code;
  public NotFoundPlanException() {
    this(PlanErrorType.NOT_FOUND_PLANS.getMessage());
  }

  public NotFoundPlanException(final String message) {
    super(message);
    this.code = PlanErrorType.NOT_FOUND_PLANS.name();
  }
}
