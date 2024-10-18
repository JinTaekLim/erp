package com.erp.erp.domain.customers.common.exception;

import lombok.Getter;

@Getter
public enum CustomersErrorType {

  Invalid_Gender("유효하지 않은 성별 입니다"),
  NOT_FOUND_CUSTOMERS("존재하지 않는 회원 입니다");

  private final String message;

  CustomersErrorType(String message) {
    this.message = message;
  }
}
