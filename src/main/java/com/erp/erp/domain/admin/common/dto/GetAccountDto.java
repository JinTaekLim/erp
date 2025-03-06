package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class GetAccountDto {

  @Schema(name = "GetAccountDto_Response" , description = "계정 조회 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "매장 ID")
    private Long accountId;

    @Schema(description = "아이디")
    private String identifier;

    @Schema(description = "점주명")
    private String name;

  }
}
