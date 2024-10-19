package com.erp.erp.domain.reservations.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddReservationsDto {

  @Schema(name = "AddReservationsDto_Request" , description = "회원 예약 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "회원 ID")
    @NotNull
    @PositiveOrZero
    private Long customersId;
    @Schema(description = "시작 시간")
    @NotNull
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    @NotNull
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;

  }
}
