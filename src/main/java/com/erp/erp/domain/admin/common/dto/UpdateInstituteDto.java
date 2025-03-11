package com.erp.erp.domain.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UpdateInstituteDto {

  @Getter
  @Builder
  public static class Request {
    @Schema(description = "매장 ID")
    @NotNull

    private Long instituteId;
    @Schema(description = "매장명")
    @NotBlank
    private String name;
  }

  @Getter
  @Builder
  public static class Response {
    @Schema(description = "매장 ID")
    private Long instituteId;
    @Schema(description = "매장명")
    private String name;
  }

}
