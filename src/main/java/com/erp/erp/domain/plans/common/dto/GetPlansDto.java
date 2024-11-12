package com.erp.erp.domain.plans.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetPlansDto {

  @Schema(name = "GetMembershipDto_Response" , description = "이용권 조회 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "이용권 ID")
    private Long id;
    @Schema(description = "이용권 이름")
    private String name;
    @Schema(description = "이용권 가격")
    private int price;

  }

}
