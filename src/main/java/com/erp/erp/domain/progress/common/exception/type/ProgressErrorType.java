
package com.erp.erp.domain.progress.common.exception.type;

import lombok.Getter;

@Getter
public enum ProgressErrorType {

  NOT_FOUND_PROGRESS("존재하지 않거나 접근 권한이 없는 진도표 입니다");

  private final String message;

  ProgressErrorType(String message) {
    this.message = message;
  }
}
