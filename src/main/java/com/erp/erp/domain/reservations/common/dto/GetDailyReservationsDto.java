package com.erp.erp.domain.reservations.common.dto;

import com.erp.erp.domain.reservations.common.entity.Reservations;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private LocalTime startTime;
    @Schema(description = "종료 시간")
    private LocalTime endTime;
    @Schema(description = "이름")
    private String name;
  }
}
