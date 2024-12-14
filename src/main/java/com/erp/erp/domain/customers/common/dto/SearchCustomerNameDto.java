package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.CustomerStatus;
import com.erp.erp.domain.customers.common.entity.Customers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchCustomerNameDto {

  @Schema(name = "SearchCustomerNameDto_Response" , description = "회원 이름 검색 반환")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "회원 ID")
    private Long customerId;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "상태 값 ( ACTIVE : 이용 가능, INACTIVE : 기간 만료 )")
    private CustomerStatus status;

    public static SearchCustomerNameDto.Response fromEntity(Customers customers) {
      return Response.builder()
          .customerId(customers.getId())
          .name(customers.getName())
          .status(customers.getStatus())
          .build();
    }
  }

}
