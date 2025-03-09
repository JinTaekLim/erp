package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UpdateAccountDto {

  @Schema(name = "UpdateAccountDto_Request" , description = "계정 수정 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "계정 ID")
    @NotNull
    private Long accountId;

    @Schema(description = "아이디")
    @NotNull
    private String identifier;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;
  }

  @Schema(name = "UpdateAccountDto_Response" , description = "계정 수정 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "계정 ID")
    private Long accountId;
    @Schema(description = "아이디")
    private String identifier;
  }


}
