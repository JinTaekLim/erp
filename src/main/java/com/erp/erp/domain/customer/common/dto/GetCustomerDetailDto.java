package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.dto.ProgressDto.ProgressResponse;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.payment.common.entity.PaymentsMethod;
import com.erp.erp.domain.plan.common.entity.CourseType;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class GetCustomerDetailDto {

  @Schema(name = "GetCustomerDetailDto_Response" , description = "고객 상세 조회 반환")
  @Getter
  @Builder
  public static class Response{
    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "성별")
    private Gender gender;
    @Schema(description = "전화번호")
    private String phone;
    @Schema(description = "생년월일")
    private LocalDate birthDate;
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
  }

  @Getter
  @Builder
  public static class PlanPaymentResponse {
    @Schema(description = "1/2종 구분")
    private LicenseType licenseType;
    @Schema(description = "이용권 이름")
    private String planName;
    @Schema(description = "이용 과정 (취득/장롱/일반)")
    private CourseType courseType;
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
  }

  @Getter
  @Builder
  public static class OtherPaymentResponse {
    @Schema(description = "결제일")
    private LocalDateTime registrationAt;
    @Schema(description = "할인 내용")
    private String content;
    @Schema(description = "할인 금액")
    private int price;
    @Schema(description = "미납 여부")
    private boolean status;
  }
}
