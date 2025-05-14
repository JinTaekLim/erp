package com.erp.erp.domain.reservation.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class GetReservationCustomerDetailsDto {

  @Schema(name = "GetCustomerDetailsDto_Response" , description = "회원 상세 정보 반환")
  @Getter
  @Builder
  public static class Response{

    @Schema(description = "예약 날짜")
    private LocalDate reservationDate;
    @Schema(description = "예약 시작 시간")
    private int startIndex;
    @Schema(description = "예약 종료 시간")
    private int endIndex;
    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "이용권")
    private String planName;
    @Schema(description = "이용권 종료 날짜")
    private LocalDateTime planEndDate;
    @Schema(description = "남은 시간")
    private int remainingTime;
    @Schema(description = "사용 시간")
    private int usedTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "진도표")
    private List<ProgressResponse> progressList;
  }

  @Getter
  @Builder
  @Schema(name = "GetReservationCustomerDetailsDto_ProgressResponse", description = "고객 예약 상세 조회 진도표 응답")
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
