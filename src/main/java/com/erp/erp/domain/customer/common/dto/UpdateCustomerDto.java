package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.entity.Progress.ProgressItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateCustomerDto {

  @Schema(name = "UpdatedCustomerInfoDto_Request", description = "회원 정보 수정 요청")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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
    private List<ProgressItem> progress;
    @Schema(description = "기타 결제")
    private List<OtherPayment> otherPayment;

    public Customer updatedCustomers(Customer customer) {
      List<com.erp.erp.domain.payment.common.entity.OtherPayment> otherPaymentList = toOtherPaymentsList();
      return customer.update(name, gender, phone, address, photoUrl, memo, birthDate,
          otherPaymentList);
    }

    private List<com.erp.erp.domain.payment.common.entity.OtherPayment> toOtherPaymentsList() {
      return this.otherPayment.stream()
              .map(otherPayment -> com.erp.erp.domain.payment.common.entity.OtherPayment.builder()
                      .content(otherPayment.getContent())
                      .status(otherPayment.isStatus())
                      .registrationAt(otherPayment.getRegistrationAt())
                      .price(otherPayment.getPrice())
                      .build())
              .toList();
    }

    public Progress toProgress() {
      return Progress.builder()
              .customerId(this.customerId)
              .progressList(this.progress)
              .build();
    }
  }

    @Schema(name = "UpdatedCustomerInfoDto_Response", description = "회원 정보 수정 반환")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
      private List<ProgressItem> progress;
      @Schema(description = "기타 결제")
      private List<OtherPayment> otherPayment;

      public static Response fromEntity(Customer customer, Progress progress) {

        List<OtherPayment> otherPaymentResponses = getOtherPaymentResponse(customer.getOtherPayments());

        return Response.builder()
                .customerId(customer.getId())
                .photoUrl(customer.getPhotoUrl())
                .name(customer.getName())
                .gender(customer.getGender())
                .birthDate(customer.getBirthDate())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .memo(customer.getMemo())
                .progress(progress.getProgressList())
                .otherPayment(otherPaymentResponses)
                .build();
      }

      private static List<OtherPayment> getOtherPaymentResponse(List<com.erp.erp.domain.payment.common.entity.OtherPayment> otherPayments) {
        return otherPayments.stream()
                .map(o -> UpdateCustomerDto.OtherPayment.builder()
                        .status(o.isStatus())
                        .content(o.getContent())
                        .price(o.getPrice())
                        .registrationAt(o.getRegistrationAt())
                        .build())
                .toList();
      }


      private List<com.erp.erp.domain.payment.common.entity.OtherPayment> getOtherPayments() {
        return this.otherPayment.stream()
                .map(o -> com.erp.erp.domain.payment.common.entity.OtherPayment.builder()
                        .status(o.status)
                        .registrationAt(o.registrationAt)
                        .content(o.content)
                        .price(o.price)
                        .build())
                .toList();
      }
    }

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class OtherPayment {

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
}
