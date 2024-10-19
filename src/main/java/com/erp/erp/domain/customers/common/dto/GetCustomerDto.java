package com.erp.erp.domain.customers.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetCustomerDto {

  @Schema(name = "GetCustomerDto_Request" , description = "회원 정보 요청")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "이름")
    private String photoUrl;
    private String name;
    private String gender;
    private String phone;

    private String plans;

    private int remainingTime;

    private int usedTime;

    private int registrationDate;

    private int tardinessCount;

    private int AbsenceCount;
    private int absenceCount;
  }

}
