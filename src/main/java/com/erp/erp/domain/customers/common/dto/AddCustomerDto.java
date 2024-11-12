package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.entity.Gender;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.payments.common.entity.PaymentsMethod;
import com.erp.erp.domain.plans.common.entity.Plans;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddCustomerDto {

  @Schema(name = "AddCustomerDto_Request" , description = "회원 등록 요청")
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Schema(description = "이용권 ID")
    @NotNull(message = "이름을 입력해주세요")
    private Long plansId;

    @Schema(description = "이름")
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Schema(description = "성별")
    @NotBlank(message = "성별을 입력해주세요")
    private String gender;

    @Schema(description = "전화번호")
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;

    @Schema(description = "주소")
    @NotBlank(message = "주소를 입력해주세요")
    private String address;

    @Schema(description = "생년월일")
    @NotNull(message = "생년월일을 입력해주세요")
    private LocalDate birthDate;

    @Schema(description = "결제 방법")
    @NotNull(message = "결제 방법을 입력해주세요")
    private PaymentsMethod paymentsMethod;

    @Schema(description = "결제 여부")
    @NotNull(message = "결제 여부를 입력해주세요")
    private boolean status;

    @Schema(description = "할인률")
    private int discount;

//    @Schema(description = "프로필 사진")
//    private MultipartFile file;


    public Customers toCustomers(Institutes institutes, String photoUrl) {
      return Customers.builder()
          .institutes(institutes)
          .name(this.name)
          .gender(Gender.getString(this.gender))
          .phone(this.phone)
          .address(this.address)
          .photoUrl(photoUrl)
          .birthDate(this.birthDate)
          .build();
    }

    public Payments toPayments(Customers customers, Plans plans) {
      return Payments.builder()
          .plans(plans)
          .customers(customers)
          .status(this.status)
          .paymentsMethod(this.paymentsMethod)
          .discount(this.discount)
          .build();
    }
  }


  @Schema(name = "AddCustomerDto_Response" , description = "회원 등록 반환")
  @Getter
  @Builder
  public static class Response {

    @Schema(description = "이용권")
    private String plans;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "전화번호")
    private String phone;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "프로필 사진")
    private String photoUrl;

    @Schema(description = "생년월일")
    private LocalDate birthDate;

    public static Response fromEntity(Payments payments, Customers customers) {
      return Response.builder()
          .plans(payments.getPlans().getName())
          .name(customers.getName())
          .gender(customers.getGender())
          .phone(customers.getPhone())
          .address(customers.getAddress())
          .birthDate(customers.getBirthDate())
          .photoUrl(customers.getPhotoUrl())
          .build();
    }
  }
}
