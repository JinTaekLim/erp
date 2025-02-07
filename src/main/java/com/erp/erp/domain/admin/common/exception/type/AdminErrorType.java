package com.erp.erp.domain.admin.common.exception.type;

import lombok.Getter;

@Getter
public enum AdminErrorType {
  UNAUTHORIZED_ACCESS("접근 권한이 존재하지 않습니다."),
  NOT_FOUND_ADMIN("존재하지 않거나 접근 권한이 없는 관리자 입니다");

  private final String message;

  AdminErrorType(String message) {
    this.message = message;
  }
}
