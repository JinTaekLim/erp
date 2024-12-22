package com.erp.erp.domain.admin.common.dto;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddInstituteDto {

  @Schema(name = "AddInstituteDto_Request" , description = "매장 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "매장명")
    @NotNull
    private String name;

    @Schema(description = "좌석 갯수")
    @Positive
    private int totalSpots;

    public Institutes toEntity() {
      return Institutes.builder().name(name).totalSpots(totalSpots).build();
    }
  }

  @Schema(name = "AddInstituteDto_Response" , description = "매장 추가 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "매장명")
    private String name;

    @Schema(description = "좌석 갯수")
    private int totalSpots;

    public static AddInstituteDto.Response fromEntity(Institutes institutes) {
      return Response.builder()
          .name(institutes.getName())
          .totalSpots(institutes.getTotalSpots())
          .build();
    }
  }


}
