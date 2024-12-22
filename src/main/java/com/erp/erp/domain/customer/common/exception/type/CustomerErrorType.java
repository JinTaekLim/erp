package com.erp.erp.domain.customer.common.exception.type;

import lombok.Getter;

@Getter
public enum CustomerErrorType {

  Invalid_Gender("유효하지 않은 성별 입니다"),
  NOT_FOUND_CUSTOMERS("존재하지 않는 회원 입니다");

  private final String message;

  CustomerErrorType(String message) {
    this.message = message;
  }
}
