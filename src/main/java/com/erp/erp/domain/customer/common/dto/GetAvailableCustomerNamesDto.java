package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
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

    public static Response fromEntity(Customer customer) {
      return Response.builder()
          .id(customer.getId())
          .name(customer.getName())
          .build();
    }
  }

}
