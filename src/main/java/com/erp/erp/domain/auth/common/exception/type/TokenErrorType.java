package com.erp.erp.domain.auth.common.exception.type;

import lombok.Getter;

@Getter
public enum TokenErrorType {

  AUTHENTICATION_REQUIRED("인증이 필요한 요청입니다."),

  // JWT
  EXPIRED_TOKEN("만료된 토큰입니다."),
  INVALID_TOKEN("유효하지 않은 토큰입니다."),
  INVALID_JWT_SIGNATURE("유효하지 않은 서명입니다."),
  NOT_FOUND_TOKEN("토큰정보를 찾을 수 없습니다."),

  // AUTH
  UN_AUTHENTICATION_ACCOUNT_EXCEPTION("인증되지 않은 사용자입니다."),
  AUTHENTICATION_NULL_ERROR("Authentication 이 NULL 입니다."),
  AUTHENTICATION_NAME_NULL_ERROR("Authentication name 이 NULL 입니다."),
  NOT_PARSED_VALUE_ERROR("파싱되지 않는 값입니다.");


  private final String message;

  TokenErrorType(String message) {
    this.message = message;
  }
}
