package com.erp.erp.domain.reservation.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddReservationDto {

  @Schema(name = "AddReservationDto_Request" , description = "회원 예약 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "회원 ID")
    @NotNull
    @PositiveOrZero
    private Long customerId;
    @Schema(description = "시작 시간")
    @NotNull
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    @NotNull
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;

  }

  @Schema(name = "AddReservationDto_Request" , description = "회원 예약 추가 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "예약 ID")
    private Long reservationId;
    @Schema(description = "회원 ID")
    private Long customerId;
    @Schema(description = "시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "좌석 번호")
    private int  seatNumber;
    @Schema(description = "메모")
    private String memo;

  }
}
