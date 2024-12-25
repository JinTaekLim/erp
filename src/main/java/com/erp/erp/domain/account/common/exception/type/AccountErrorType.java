package com.erp.erp.domain.account.common.exception.type;

import lombok.Getter;

@Getter
public enum AccountErrorType {
  INVALID_CREDENTIALS("아이디 혹은 비밀번호를 잘못 입력하셨습니다."),

  ACCOUNT_INSTITUTE_NOT_FOUND("계정과 연결된 매장 정보를 찾을 수 없습니다.");

  private final String message;

  AccountErrorType(String message) {
    this.message = message;
  }
}
