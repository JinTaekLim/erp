package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.Customers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetCustomerDto {

  @Schema(name = "GetCustomerDto_Response" , description = "회원 정보 반환")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "성별")
    private String gender;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "이용권")
    private String plans;
    @Schema(description = "남은 시간")
    private int remainingTime;
    @Schema(description = "사용 시간")
    private int usedTime;
    @Schema(description = "등록 날짜")
    private int registrationDate;
    @Schema(description = "지각 횟수")
    private int tardinessCount;
    @Schema(description = "결석 횟수")
    private int absenceCount;

    public static Response fromEntity(Customers customers) {
      return Response.builder()
          .photoUrl(customers.getPhotoUrl())
          .name(customers.getName())
          .gender(String.valueOf(customers.getGender()))
          .phone(customers.getPhone())
          .plans(customers.getInstitutes().getName())
          .remainingTime(0)
          .usedTime(0)
          .registrationDate(0)
          .tardinessCount(0)
          .absenceCount(0)
          .build();
    }
  }
}
