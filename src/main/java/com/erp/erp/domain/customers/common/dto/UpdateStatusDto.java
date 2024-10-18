package com.erp.erp.domain.customers.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateStatusDto {

  @Schema(name = "UpdateStatusDto_Request" , description = "회원 상태 변경 요청")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Schema(description = "회원 ID")
    @NotBlank(message = "회원 ID를 입력해주세요")
    private Long customersId;

    @Schema(description = "상태 ( true : 회원 삭제 / false : 회원 복구 )")
    @NotBlank(message = "상태를 입력해주세요.")
    private Boolean status;
  }

}
