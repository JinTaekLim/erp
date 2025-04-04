package com.erp.erp.domain.reservation.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class GetDailyReservationDto {
  @Schema(name = "GetDailyReservationDto_Response" , description = "하루 예약 조회 반환")
  @Builder
  @Getter
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationId;
    @Schema(description = "시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "좌석 번호")
    private int seatNumber;
    @Schema(description = "이름")
    private String name;
  }
}
