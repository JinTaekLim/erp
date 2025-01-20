package com.erp.erp.domain.customer.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProgressDto {

  @Getter
  @Builder
  @Schema(name = "ProgressDto_Request", description = "진도표 업데이트 요청")
  public static class Request {
    private List<AddProgress> addProgresses;
    private List<UpdateProgress> updateProgresses;
    private List<DeleteProgress> deleteProgresses;
  }

  @Getter
  @Builder
  public static class AddProgress {

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;
  }

  @Getter
  @Builder
  public static class UpdateProgress {

    @Schema(description = "진도표 ID")
    @NotNull
    private Long progressId;

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DeleteProgress {

    @Schema(description = "진도표 ID")
    @NotNull
    private Long progressId;

  }

  @Getter
  @Builder
  public static class ProgressResponse {

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
