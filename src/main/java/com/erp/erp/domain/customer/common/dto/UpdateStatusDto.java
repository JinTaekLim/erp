package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UpdateStatusDto {

  @Schema(name = "UpdateStatusDto_Request" , description = "회원 상태 변경 요청")
  @Getter
  @Builder
  public static class Request {

    @Schema(description = "회원 ID")
    @NotNull(message = "회원 ID를 입력해주세요")
    private Long customerId;

    @Schema(description = "ACTIVE : 이용 가능, INACTIVE : 기간 만료, DELETED : 삭제된 회원")
    @NotNull(message = "상태를 입력해주세요.")
    private CustomerStatus status;
  }

}
