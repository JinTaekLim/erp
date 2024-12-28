package com.erp.erp.domain.admin.common.dto;

import com.erp.erp.domain.institute.common.entity.Institute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

public class AddInstituteDto {

  @Schema(name = "AddInstituteDto_Request" , description = "매장 추가 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "매장명", example = "1호점")
    @NotBlank
    private String name;

    @Schema(description = "좌석 갯수", example = "3")
    @Positive
    private int totalSeat;

    @Schema(description = "영업 시작 시간", example = "08:00:00.00")
    @NotNull
    private LocalTime openTime;

    @Schema(description = "영업 종료 시간", example = "22:00:00.00")
    @NotNull
    private LocalTime closeTime;

    public Institute toEntity() {
      return Institute.builder()
          .name(name)
          .totalSeat(totalSeat)
          .openTime(openTime)
          .closeTime(closeTime)
          .build();
    }
  }

  @Schema(name = "AddInstituteDto_Response" , description = "매장 추가 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "매장명")
    private String name;

    @Schema(description = "좌석 갯수")
    private int totalSeat;

    @Schema(description = "영업 시작 시간")
    private LocalTime openTime;

    @Schema(description = "영업 종료 시간")
    private LocalTime closeTime;

    public static AddInstituteDto.Response fromEntity(Institute institute) {
      return Response.builder()
          .name(institute.getName())
          .totalSeat(institute.getTotalSeat())
          .openTime(institute.getOpenTime())
          .closeTime(institute.getCloseTime())
          .build();
    }
  }


}
