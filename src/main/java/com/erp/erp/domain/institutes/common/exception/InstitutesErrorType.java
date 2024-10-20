package com.erp.erp.domain.institutes.common.exception;

import lombok.Getter;

@Getter
public enum InstitutesErrorType {
  INSTITUTES_NOT_FOUND_IN_CUSTOMERS("매장에 등록 되어 있지 않은 회원 입니다");

  private final String message;

  InstitutesErrorType(String message) {
    this.message = message;
  }
}
