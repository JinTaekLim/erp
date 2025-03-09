package com.erp.erp.domain.plan.common.exception;

import lombok.Getter;

@Getter
public enum PlanErrorType {
  NOT_FOUND_PLANS("이용권을 찾을 수 없습니다.");

  private final String message;

  PlanErrorType(String message) {
    this.message = message;
  }
}
