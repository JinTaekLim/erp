package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class LoginDto {

  @Schema(name = "LoginDto_Request" , description = "로그인 요청")
  @Getter
  @Builder
  public static class Request {

    @Schema(description = "아이디")
    @NotBlank(message = "아이디를 입력해주세요")
    private String identifier;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
  }


}
