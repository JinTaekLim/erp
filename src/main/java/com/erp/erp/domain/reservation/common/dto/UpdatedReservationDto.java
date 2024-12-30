package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.reservation.common.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class UpdatedReservationDto {

  @Schema(name = "UpdateReservationDto_Request" , description = "회원 예약 변경 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "예약 ID")
    @NotNull
    @PositiveOrZero
    private Long reservationId;
    @Schema(description = "시작 시간")
    @NotNull
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    @NotNull
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    @NotNull
    private int seatNumber;

  }

  @Schema(name = "UpdateReservationDto_Response" , description = "회원 예약 변경 반환")
  @Builder
  @Getter
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationId;
    @Schema(description = "시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    private int seatNumber;

    public static Response fromEntity(Reservation reservation) {
      return Response.builder()
          .reservationId(reservation.getId())
          .startTime(reservation.getStartTime())
          .endTime(reservation.getEndTime())
          .memo(reservation.getMemo())
          .seatNumber(reservation.getSeatNumber())
          .build();
    }
  }

}
