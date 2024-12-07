package com.erp.erp.domain.plan.common.dto;

import com.erp.erp.domain.plan.common.entity.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetPlanDto {

  @Schema(name = "GetPlans_Request" , description = "이용권 조회 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @Schema(description = "이용권 구분", example = "null, TYPE_1, TYPE_2")
    private LicenseType licenseType;
  }

  @Schema(name = "GetPlans_Response" , description = "이용권 조회 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "이용권 ID")
    private Long id;
    @Schema(description = "이용권 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String name;
    @Schema(description = "이용권 가격")
    private int price;

  }

}
