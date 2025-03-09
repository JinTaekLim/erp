package com.erp.erp.domain.plan.common.dto;

import com.erp.erp.domain.plan.common.entity.CourseType;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class GetPlanDto {

  @Schema(name = "GetPlans_Response" , description = "이용권 조회 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "이용권 ID")
    private Long id;
    @Schema(description = "이용권 구분")
    private PlanType planType;
    @Schema(description = "1/2종 구분")
    private LicenseType licenseType;
    @Schema(description = "이용 과정 (취득/장롱/일반)")
    private CourseType courseType;
    @Schema(description = "이용권 이름")
    private String planName;
    @Schema(description = "이용권 가격")
    private int price;
  }

}
