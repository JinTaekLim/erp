package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto.ProgressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class UpdatedReservationDto {

  @Schema(name = "UpdateReservationDto_Request" , description = "회원 예약 변경 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "예약 ID")
    @NotNull
    @PositiveOrZero
    private Long reservationId;
    @Schema(description = "시작 시간")
    @NotNull
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    @NotNull
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    @NotNull
    private int seatNumber;
    @Schema(description = "진도표")
    private ProgressDto.Request progressList;

  }

  @Schema(name = "UpdateReservationDto_Response" , description = "회원 예약 변경 반환")
  @Builder
  @Getter
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationId;
    @Schema(description = "시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    private int seatNumber;
    @Schema(description = "진도표")
    private List<ProgressResponse> progressList;
  }

}
