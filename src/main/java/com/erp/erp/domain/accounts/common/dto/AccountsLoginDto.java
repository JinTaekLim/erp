package com.erp.erp.domain.accounts.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountsLoginDto {

  @Schema(name = "AccountsLoginDto_Request" , description = "로그인 요청")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Schema(description = "아이디")
    @NotBlank(message = "아이디를 입력해주세요")
    private String account;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
  }

  @Schema(name = "AccountsLoginDto_Response", description = "로그인 응답")
  @Builder
  public static class Response {

    @Schema(description = "액세스 토큰")
    private String accessToken;
    @Schema(description = "리프레쉬 토큰")
    private String refreshToken;
  }

}
