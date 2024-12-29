package com.erp.erp.domain.customer.common.exception.type;

import lombok.Getter;

@Getter
public enum CustomerErrorType {

  NOT_FOUND_CUSTOMERS("존재하지 않거나 접근 권한이 없는 회원 입니다");

  private final String message;

  CustomerErrorType(String message) {
    this.message = message;
  }
}
