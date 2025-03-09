package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

public class GetInstituteDto {

  @Schema(name = "GetInstituteDto_Response" , description = "매장 조회 반환")
  @Getter
  @Builder
  public static class Response{
    @Schema(description = "매장 ID")
    private Long id;
    @Schema(description = "매장 이름")
    private String name;
    @Schema(description = "영업 시작 시간")
    private LocalTime openTime;
    @Schema(description = "영업 종료 시간")
    private LocalTime closeTime;
  }
}
