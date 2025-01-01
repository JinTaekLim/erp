package com.erp.erp.domain.institute.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateTotalSeatDto {

  @Schema(name = "UpdateTotalSeatDto_Request" , description = "좌석 갯수 변경 요청")
  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    @Schema(description = "좌석 갯수")
    @Positive(message = "좌석 갯수는 1개 이상이어야 합니다.")
    private int totalSeat;

  }

  @Schema(name = "UpdateTotalSeatDto_Response", description = "좌석 갯수 변경 응답")
  @Getter
  @Builder
  public static class Response {

    @Schema(description = "매장 이름")
    private String name;
    @Schema(description = "좌석 갯수")
    private int totalSeat;
  }

}
