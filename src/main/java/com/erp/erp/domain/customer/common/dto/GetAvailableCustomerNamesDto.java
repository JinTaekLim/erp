package com.erp.erp.domain.customer.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class GetAvailableCustomerNamesDto {

  @Schema(name = "GetAvailableCustomerNamesDto_Response" , description = "이용 가능 고객 이름 조회 반환")
  @Getter
  @Builder
  public static class Response{

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "이름")
    private String name;
  }

}
