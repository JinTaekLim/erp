package com.erp.erp.domain.reservations.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetReservationCustomerDetailsDto {

  @Schema(name = "GetCustomerDetailsDto_Response" , description = "회원 상세 정보 반환")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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
    private String plans;
    @Schema(description = "이용권 종료 날짜")
    private LocalDate endDate;
    @Schema(description = "남은 시간")
    private int remainingTime;
    @Schema(description = "사용 시간")
    private int usedTime;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "진도표")
    private List<LessonProgress> lessonProgressList;

    public static Response fromEntity(
        Reservations reservations
    ) {
      Customer customer = reservations.getCustomer();
      PlanPayment planPayment = customer.getPlanPayment();
      Plan plan = planPayment.getPlan();

      LocalDateTime registrationAt = planPayment.getRegistrationAt();
      int availablePeriod = plan.getAvailablePeriod();

      LocalDate endDate = registrationAt.plusDays(availablePeriod).toLocalDate();


      return Response.builder()
          .startTime(reservations.getStartTime())
          .endTime(reservations.getEndTime())
          .photoUrl(customer.getPhotoUrl())
          .name(customer.getName())
          .gender(customer.getGender())
          .phone(customer.getPhone())
          .plans(plan.getName())
          .endDate(endDate)
          .remainingTime(0)
          .usedTime(0)
          .memo("null")
          .lessonProgressList(null)
          .build();
    }
  }

  private static class LessonProgress{
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "내용")
    private String content;

  }

}
