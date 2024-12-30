package com.erp.erp.domain.admin.common.dto;

import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

public class AddPlanDto {

  @Schema(name = "AddMembershipDto_Request" , description = "이용권 추가 요청")
  @Builder
  @Getter
  public static class Request{


    @Schema(description = "이용권 구분", example = "TIME_BASED, PERIOD_BASED")
    @NotNull
    private PlanType planType;

    @Schema(description = "1/2종 구분", example = "TYPE_1, TYPE_2")
    @NotNull
    private LicenseType licenseType;

    @Schema(description = "이용권 이름", example = "5시간 이용권")
    @NotNull
    private String name;
    @Schema(description = "이용권 가격", example = "5000")
    @Positive(message = "-1 이하의 값은 입력할 수 없습니다.")
    private int price;

    @Schema(description = "이용권 시간", example = "5")
    @NotNull
    @PositiveOrZero
    private int availableTime;

    @Schema(description = "이용권 기간(일)", example = "30")
    @NotNull
    @PositiveOrZero
    private int availablePeriod;

  }

  @Schema(name = "AddMembershipDto_Response" , description = "이용권 추가 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "이용권 구분")
    private PlanType planType;
    @Schema(description = "1/2종 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String name;
    @Schema(description = "이용권 가격")
    private int price;

  }

}
