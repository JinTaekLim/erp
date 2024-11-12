package com.erp.erp.domain.plans.common.exception;

import lombok.Getter;

@Getter
public enum PlansErrorType {
  NOT_FOUND_PLANS("이용권을 찾을 수 없습니다.");

  private final String message;

  PlansErrorType(String message) {
    this.message = message;
  }
}
