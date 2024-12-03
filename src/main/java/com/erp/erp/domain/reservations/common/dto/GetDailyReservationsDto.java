package com.erp.erp.domain.reservations.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetDailyReservationsDto {
  @Schema(name = "GetDailyReservationsDto_Response" , description = "하루 예약 조회 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationsId;
    @Schema(description = "시작 시간")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @Schema(description = "종료 시간")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @Schema(description = "좌석 번호")
    private int seatNumber;
    @Schema(description = "이름")
    private String name;
  }
}
