package com.erp.erp.domain.reservations.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeleteReservationsDto {
  @Schema(name = "DeleteReservationsDto_Request" , description = "회원 예약 삭제 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "예약 ID")
    @NotNull
    @PositiveOrZero
    private Long reservationsId;
  }


}
