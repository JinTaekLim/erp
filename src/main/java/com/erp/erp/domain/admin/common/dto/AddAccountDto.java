package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

public class AddAccountDto {

  @Schema(name = "AddAccountDto_Request" , description = "계정 추가 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "매장 ID")
    @Positive
    private Long instituteId;

    @Schema(description = "아이디")
    @NotNull
    private String identifier;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;
  }

  @Schema(name = "AddAccountDto_Response" , description = "계정 추가 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "매장 ID")
    private Long instituteId;

    @Schema(description = "아이디")
    private String identifier;

    @Schema(description = "생성자")
    private String createdId;
  }


}
