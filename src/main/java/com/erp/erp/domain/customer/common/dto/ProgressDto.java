package com.erp.erp.domain.customer.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class ProgressDto {

  @Getter
  @Builder
  @Schema(name = "ProgressDto_Request", description = "진도표 요청")
  public static class Request {

    @Schema(description = "진도표 ID")
    private Long progressId;

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;

    @Schema(description = "삭제 여부")
    private boolean deleted;
  }

  @Getter
  @Builder
  @Schema(name = "ProgressDto_Response", description = "진도표 응답")
  public static class Response {

    @Schema(description = "진도표 ID")
    private Long progressId;

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;

  }
}
