package com.erp.erp.domain.institute.common.exception.type;

import lombok.Getter;

@Getter
public enum InstituteErrorType {
  NOT_FOUND_INSTITUTE("등록 되어 있지 않은 매장입니다.");
//  INSTITUTE_NOT_FOUND_IN_CUSTOMERS("매장에 등록 되어 있지 않은 회원 입니다");

  private final String message;

  InstituteErrorType(String message) {
    this.message = message;
  }
}
