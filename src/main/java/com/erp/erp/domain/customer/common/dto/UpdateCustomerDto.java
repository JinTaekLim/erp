package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.payment.common.entity.PaymentsMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

public class UpdateCustomerDto {

  @Schema(name = "UpdatedCustomerInfoDto_Request", description = "회원 정보 수정 요청")
  @Getter
  @Builder
  public static class Request {

    @Schema(description = "회원 ID")
    @NotNull
    private Long customerId;
    @Schema(description = "프로필 URL")
    private String photoUrl;
    @Schema(description = "이름")
    @NotNull
    private String name;
    @Schema(description = "성별")
    @NotNull
    private Gender gender;
    @Schema(description = "생일")
    @NotNull
    private LocalDate birthDate;
    @Schema(description = "전화번호")
    @NotNull
    private String phone;
    @Schema(description = "주소")
    @NotNull
    private String address;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "진도표")
    private List<ProgressResponse> progress;
    @Schema(description = "기타 결제")
    private List<OtherPaymentResponse> otherPayment;
  }

    @Schema(name = "UpdatedCustomerInfoDto_Response", description = "회원 정보 수정 반환")
    @Getter
    @Builder
    public static class Response {

      @Schema(description = "회원 ID")
      private Long customerId;
      @Schema(description = "프로필 URL")
      private String photoUrl;
      @Schema(description = "이름")
      private String name;
      @Schema(description = "성별")
      private Gender gender;
      @Schema(description = "생일")
      @NotNull
      private LocalDate birthDate;
      @Schema(description = "전화번호")
      private String phone;
      @Schema(description = "주소")
      private String address;
      @Schema(description = "메모")
      private String memo;
      @Schema(description = "진도표")
      private List<ProgressResponse> progress;
      @Schema(description = "기타 결제")
      private List<OtherPaymentResponse> otherPayment;
    }

  @Builder
  @Getter
  public static class OtherPaymentResponse {

    @Schema(description = "결제 방법")
    @NotNull(message = "결제 방법을 입력해주세요")
    private PaymentsMethod paymentsMethod;

    @Schema(description = "결제 일자")
    @NotNull(message = "결제 일자를 입력해주세요.")
    private LocalDateTime registrationAt;

    @Schema(description = "결제 내용")
    @NotNull(message = "결제 내용을 입력해주세요")
    private String content;

    @Schema(description = "결제 금액")
    @Positive(message = "-1 이하의 값은 입력될 수 없습니다.")
    private int price;

    @Schema(description = "결제 여부")
    @NotNull(message = "결제 여부를 입력해주세요")
    private boolean status;
  }

  @Builder
  @Getter
  public static class ProgressResponse {

    @Schema(description = "날짜")
    @NotNull
    private LocalDate date;

    @Schema(description = "내용")
    @NotNull
    private String content;
  }
}
