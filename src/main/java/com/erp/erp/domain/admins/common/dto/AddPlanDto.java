package com.erp.erp.domain.admins.common.dto;

import com.erp.erp.domain.plan.common.entity.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddPlanDto {

  @Schema(name = "AddMembershipDto_Request" , description = "이용권 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{


    @Schema(description = "이용권 구분")
    @NotNull
    private LicenseType licenseType;

    @Schema(description = "이용권 이름")
    @NotNull
    private String name;
    @Schema(description = "이용권 가격")
    @Positive(message = "-1 이하의 값은 입력할 수 없습니다.")
    private int price;

    @Schema(description = "이용권 시간")
    @NotNull
    private int availableTime;

    @Schema(description = "이용권 기간")
    @NotNull
    private int availablePeriod;

  }

  @Schema(name = "AddMembershipDto_Response" , description = "이용권 추가 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "이용권 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String name;
    @Schema(description = "이용권 가격")
    private int price;

  }

}
