package com.erp.erp.domain.institutes.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateTotalSpotsDto {

  @Schema(name = "updateTotalSpotsDto_Request" , description = "좌석 갯수 변경 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Schema(description = "좌석 갯수")
    @Positive(message = "좌석 갯수는 1개 이상이어야 합니다.")
    private int num;

  }

  @Schema(name = "updateTotalSpotsDto_Response", description = "좌석 갯수 변경 응답")
  @Getter
  @Builder
  public static class Response {

    @Schema(description = "매장 아이디")
    private Long id;
    @Schema(description = "매장 이름")
    private String name;
    @Schema(description = "좌석 갯수")
    private int num;
  }

}
