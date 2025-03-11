package com.erp.erp.domain.reservation.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class AddReservationDto {

  @Schema(name = "AddReservationDto_Request" , description = "회원 예약 추가 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "회원 ID")
    @NotNull
    @PositiveOrZero
    private Long customerId;
    @Schema(description = "예약 날짜")
    @NotNull
    private LocalDate reservationDate;
    @Schema(description = "시작 시간")
    @NotNull
    private int startIndex;
    @Schema(description = "종료 시간")
    @NotNull
    private int endIndex;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    @Min(value = 1, message = "좌석 번호는 1 이상의 값이어야 합니다.")
    private int seatNumber;
  }
}
