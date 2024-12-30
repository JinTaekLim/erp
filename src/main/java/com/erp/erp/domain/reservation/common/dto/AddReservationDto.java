package com.erp.erp.domain.reservation.common.dto;


import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class AddReservationDto {

  @Schema(name = "AddReservationDto_Request" , description = "회원 예약 추가 요청")
  @Builder
  @Getter
  public static class Request{

    @Schema(description = "회원 ID")
    @NotNull
    @PositiveOrZero
    private Long customerId;
    @Schema(description = "시작 시간")
    @NotNull
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    @NotNull
    private LocalDateTime endTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "좌석 번호")
    @Min(value = 1, message = "좌석 번호는 1 이상의 값이어야 합니다.")
    private int seatNumber;


    public Reservation toEntity(Institute institute, Customer customer) {
      return Reservation.builder()
          .institute(institute)
          .customer(customer)
          .startTime(this.startTime)
          .endTime(this.endTime)
          .memo(this.memo)
          .seatNumber(this.seatNumber)
          .build();
    }
  }

  @Schema(name = "AddReservationDto_Request" , description = "회원 예약 추가 반환")
  @Builder
  @Getter
  public static class Response{

    @Schema(description = "예약 ID")
    private Long reservationId;
    @Schema(description = "회원 ID")
    private Long customerId;
    @Schema(description = "시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "좌석 번호")
    private int  seatNumber;
    @Schema(description = "메모")
    private String memo;

    public static Response fromEntity(Reservation reservation){
      return AddReservationDto.Response.builder()
          .reservationId(reservation.getId())
          .customerId(reservation.getCustomer().getId())
          .startTime(reservation.getStartTime())
          .endTime(reservation.getEndTime())
          .memo(reservation.getMemo())
          .seatNumber(reservation.getSeatNumber())
          .build();
    }
  }
}
