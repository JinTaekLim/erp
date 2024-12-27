package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.entity.Progress.ProgressItem;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PaymentsMethod;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetCustomerDetailDto {

  @Schema(name = "GetCustomerDetailDto_Response" , description = "고객 상세 조회 반환")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{
    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "성별")
    private Gender gender;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "방문 경로")
    private String visitPath;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "진도표")
    List<ProgressResponse> progressList;
    @Schema(description = "이용권 결제")
    private PlanPaymentResponse planPayment;
    @Schema(description = "기타 결제")
    private List<OtherPaymentResponse> otherPayment;

    public static Response fromEntity(Customer customer, Progress progress) {

      List<ProgressResponse> progressResponseList = (progress != null && progress.getProgressList() != null && !progress.getProgressList().isEmpty())
          ? progress.getProgressList().stream()
          .map(ProgressResponse::fromEntity)
          .toList()
          : null;

      List<OtherPaymentResponse> otherPaymentResponseList = (customer.getOtherPayments() != null) ?
          customer.getOtherPayments().stream()
          .map(OtherPaymentResponse::fromEntity)
          .toList()
          : null;


      return Response.builder()
          .photoUrl(customer.getPhotoUrl())
          .name(customer.getName())
          .gender(customer.getGender())
          .phone(customer.getPhone())
          .address(customer.getAddress())
          .visitPath(null)
          .memo(customer.getMemo())
          .progressList(progressResponseList)
          .planPayment(PlanPaymentResponse.fromEntity(customer.getPlanPayment()))
          .otherPayment(otherPaymentResponseList)
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PlanPaymentResponse {
    @Schema(description = "1/2종 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String planName;
    @Schema(description = "이용권 금액")
    private int planPrice;
    @Schema(description = "이용권 할인률")
    private double discountRate;
    @Schema(description = "이용권 할인 금액")
    private int discountPrice;
    @Schema(description = "이용권 결제 방법")
    private PaymentsMethod paymentsMethod;
    @Schema(description = "결제일")
    private LocalDateTime registrationAt;
    @Schema(description = "결제 금액")
    private int paymentTotal;
    @Schema(description = "미납 여부")
    private boolean status;

    public static PlanPaymentResponse fromEntity(PlanPayment planPayment) {

      int planPrice = planPayment.getPlan().getPrice();
      int discountPrice = (int) (planPrice * planPayment.getDiscountRate());
      int paymentTotal = planPrice - discountPrice;

      return PlanPaymentResponse.builder()
          .licenseType(planPayment.getPlan().getLicenseType())
          .planName(planPayment.getPlan().getName())
          .planPrice(planPayment.getPlan().getPrice())
          .discountRate(planPayment.getDiscountRate())
          .discountPrice(discountPrice)
          .paymentsMethod(planPayment.getPaymentsMethod())
          .registrationAt(planPayment.getRegistrationAt())
          .paymentTotal(paymentTotal)
          .status(planPayment.isStatus())
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OtherPaymentResponse {
    @Schema(description = "결제일")
    private LocalDateTime registrationAt;
    @Schema(description = "할인 내용")
    private String content;
    @Schema(description = "할인 금액")
    private int price;
    @Schema(description = "미납 여부")
    private boolean status;

    public static OtherPaymentResponse fromEntity(OtherPayment otherPayment) {
      return OtherPaymentResponse.builder()
          .registrationAt(otherPayment.getRegistrationAt())
          .content(otherPayment.getContent())
          .price(otherPayment.getPrice())
          .status(otherPayment.isStatus())
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProgressResponse {
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "내용")
    private String content;

    public static ProgressResponse fromEntity(ProgressItem progress) {
      return ProgressResponse.builder()
          .content(progress.getContent())
          .date(progress.getDate())
          .build();
    }
  }
}
