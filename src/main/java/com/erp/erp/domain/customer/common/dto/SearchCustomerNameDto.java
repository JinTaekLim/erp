package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class SearchCustomerNameDto {

  @Schema(name = "SearchCustomerNameDto_Response" , description = "회원 이름 검색 반환")
  @Getter
  @Builder
  public static class Response{

    @Schema(description = "회원 ID")
    private Long customerId;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "상태 값 ( ACTIVE : 이용 가능, INACTIVE : 기간 만료 )")
    private CustomerStatus status;
  }

}
