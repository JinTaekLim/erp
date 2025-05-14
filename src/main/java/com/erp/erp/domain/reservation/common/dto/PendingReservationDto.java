package com.erp.erp.domain.reservation.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PendingReservationDto {

  @NotNull
  private String day;
  @NotNull
  private int startIndex;
  @NotNull
  private int endIndex;

}
