package com.erp.erp.domain.payment.common.exception.type;

import lombok.Getter;

@Getter
public enum PaymentsErrorType {

  NOT_FOUND_PAYMENTS("결제 내역이 존재하지 않습니다");

  private final String message;

  PaymentsErrorType(String message) {
    this.message = message;
  }
}
