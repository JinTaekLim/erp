package com.erp.erp.domain.institute.common.dto;

import com.erp.erp.domain.institute.common.entity.Institute;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

public class GetInstituteInfoDto {

  @Schema(name = "GetInstituteInfoDto_Response", description = "매장 정보 조회 반환")
  @Getter
  @Builder
  public static class Response {

    @Schema(description = "좌석 갯수")
    private int totalSeat;
    @Schema(description = "영업 시작 시간")
    private LocalTime openTime;
    @Schema(description = "영업 종료 시간")
    private LocalTime closeTime;

    public static GetInstituteInfoDto.Response fromEntity(Institute institute) {
      return Response.builder()
          .totalSeat(institute.getTotalSeat())
          .openTime(institute.getOpenTime())
          .closeTime(institute.getCloseTime())
          .build();
    }
  }

}
