package com.erp.erp.domain.reservation.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetReservationByTimeDto {
  @Schema(name = "GetReservationByTimeResponse" , description = "특정 시간 예약 조회 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationId;
    @Schema(description = "시작 시간")
    private LocalTime startTime;
    @Schema(description = "종료 시간")
    private LocalTime endTime;
    @Schema(description = "이름")
    private String name;
  }
}
