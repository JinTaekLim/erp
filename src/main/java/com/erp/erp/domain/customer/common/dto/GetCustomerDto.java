package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.PlanType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class GetCustomerDto {

  @Schema(name = "GetCustomerDto_Response" , description = "회원 정보 반환")
  @Getter
  @Builder
  public static class Response{

    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "성별")
    private Gender gender;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "1/2종 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String planName;
    @Schema(description = "이용권 구분 (시간/기간제)")
    private PlanType planType;
    @Schema(description = "남은 시간")
    private double remainingTime;
    @Schema(description = "남은 기간")
    private int remainingPeriod;
    @Schema(description = "사용 시간")
    private double usedTime;
    @Schema(description = "등록 날짜")
    private LocalDateTime registrationDate;
    @Schema(description = "지각 횟수")
    private int tardinessCount;
    @Schema(description = "결석 횟수")
    private int absenceCount;

    public static Response fromEntity(Customer customer) {

      LocalDateTime registrationDate = customer.getPlanPayment().getRegistrationAt();

      double usedTime = 0;
      double remainingTime = 0;
      int remainingPeriod = 0;


      return Response.builder()
          .photoUrl(customer.getPhotoUrl())
          .name(customer.getName())
          .gender(customer.getGender())
          .phone(customer.getPhone())
          .licenseType(customer.getPlanPayment().getPlan().getLicenseType())
          .planName(customer.getPlanPayment().getPlan().getName())
          .planType(customer.getPlanPayment().getPlan().getPlanType())
          .remainingTime(remainingTime)
          .remainingPeriod(remainingPeriod)
          .usedTime(usedTime)
          .registrationDate(registrationDate)
          .tardinessCount(0)
          .absenceCount(0)
          .build();
    }
  }
}
