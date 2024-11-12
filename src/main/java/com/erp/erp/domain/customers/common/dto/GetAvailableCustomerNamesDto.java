package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.Customers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetAvailableCustomerNamesDto {

  @Schema(name = "GetAvailableCustomerNamesDto_Response" , description = "이용 가능 고객 이름 조회 반환")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "이름")
    private String name;

    public static Response fromEntity(Customers customers) {
      return Response.builder()
          .id(customers.getId())
          .name(customers.getName())
          .build();
    }
  }

}
