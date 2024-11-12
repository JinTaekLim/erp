package com.erp.erp.domain.membership.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddMembershipDto {

  @Schema(name = "AddMembershipDto_Request" , description = "이용권 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "이용권 이름")
    @NotNull
    private String name;
    @Schema(description = "이용권 가격")
    @NotNull
    private int price;

  }

  @Schema(name = "AddMembershipDto_Response" , description = "이용권 추가 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "이용권 이름")
    private String name;
    @Schema(description = "이용권 가격")
    private int price;

  }

}
