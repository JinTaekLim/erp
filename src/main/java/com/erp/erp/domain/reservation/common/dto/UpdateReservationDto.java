package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class UpdateReservationDto {

  @Schema(name = "UpdateReservationDto_Request" , description = "회원 예약 변경 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "예약 ID")
    @NotNull
    @PositiveOrZero
    private Long reservationId;

    @Schema(description = "예약 날짜")
    @NotNull
    private LocalDate reservationDate;

    @Schema(description = "시작 시간")
    @NotNull
    private int startIndex;

    @Schema(description = "종료 시간")
    @NotNull
    private int endIndex;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "좌석 번호")
    @NotNull
    private int seatNumber;

    @Schema(description = "출석 상태 ( 출석/정상 : NORMAL, 지각 : LATE, 결석 : ABSENT ")
    @NotNull
    private AttendanceStatus attendanceStatus;

    @Schema(description = "진도표")
    private List<ProgressRequest> progressList;

  }

  @Schema(name = "UpdateReservationDto_Response" , description = "회원 예약 변경 반환")
  @Builder
  @Getter
  public static class Response {

    @Schema(description = "예약 번호")
    private Long reservationId;

    @Schema(description = "예약 날짜")
    private LocalDate reservationDate;

    @Schema(description = "시작 시간")
    private int startIndex;

    @Schema(description = "종료 시간")
    private int endIndex;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "좌석 번호")
    private int seatNumber;

    @Schema(description = "출석 상태 ( 출석/정상 : NORMAL, 지각 : LATE, 결석 : ABSENT ")
    private AttendanceStatus attendanceStatus;

    @Schema(description = "진도표")
    private List<ProgressResponse> progressList;
  }

  @Getter
  @Builder
  @Schema(name = "UpdateReservationDto_ProgressRequest", description = "예약 수정 진도표 요청")
  public static class ProgressRequest {

    @Schema(description = "진도표 ID")
    @NotNull
    private Long progressId;

    @Schema(description = "내용")
    @NotNull
    private String content;

  }

  @Getter
  @Builder
  @Schema(name = "UpdateReservationDto_ProgressResponse", description = "예약 수정 진도표 응답")
  public static class ProgressResponse {

    @Schema(description = "진도표 ID")
    private Long progressId;

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;

    @Schema(description = "사용 시간")
    @NotNull
    private Double usedTime;

  }

}
