package com.erp.erp.domain.reservation.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UpdatedSeatNumberDto {

  @Schema(name = "UpdatedSeatNumberDto_Request" , description = "예약 좌석 변경 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "예약 ID")
    @NotNull
    private Long reservationId;
    @Schema(description = "좌석 번호")
    @NotNull
    private int seatNumber;

  }

  @Schema(name = "UpdatedSeatNumberDto_Response" , description = "예약 좌석 변경 반환")
  @Builder
  @Getter
  public static class Response {

    @Schema(description = "예약 ID")
    private Long reservationId;
    @Schema(description = "좌석 번호")
    private int seatNumber;
  }

}
