package com.erp.erp.domain.account.common.exception.type;

import lombok.Getter;

@Getter
public enum AccountErrorType {
  INVALID_CREDENTIALS("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");

  private final String message;

  AccountErrorType(String message) {
    this.message = message;
  }
}
