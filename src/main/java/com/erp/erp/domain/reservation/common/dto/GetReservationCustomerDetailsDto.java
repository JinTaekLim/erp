package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "예약 시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "예약 종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "성별")
    private Gender gender;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "이용권")
    private String planName;
    @Schema(description = "이용권 종료 날짜")
    private LocalDateTime endDate;
    @Schema(description = "남은 시간")
    private int remainingTime;
    @Schema(description = "사용 시간")
    private int usedTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "진도표")
    private List<ProgressResponse> progress;

    public static Response fromEntity(
        Reservation reservation
    ) {
      Customer customer = reservation.getCustomer();
      Plan plan = customer.getPlanPayment().getPlan();


      LocalDateTime registrationAt = customer.getPlanPayment().getRegistrationAt().withNano(0);
      int availablePeriod = plan.getAvailablePeriod();
      LocalDateTime endDate = registrationAt.plusDays(availablePeriod);

      return Response.builder()
          .startTime(reservation.getStartTime())
          .endTime(reservation.getEndTime())
          .photoUrl(customer.getPhotoUrl())
          .name(customer.getName())
          .gender(customer.getGender())
          .phone(customer.getPhone())
          .planName(plan.getName())
          .endDate(endDate)
          .remainingTime(0)
          .usedTime(0)
          .memo(customer.getMemo())
          .progress(null)
          .build();
    }
  }

  private static class ProgressResponse{
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "내용")
    private String content;

  }

}
