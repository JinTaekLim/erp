package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.entity.Gender;
import com.erp.erp.domain.customers.common.entity.Progress;
import com.erp.erp.domain.customers.common.entity.Progress.ProgressItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdatedCustomerInfoDto {

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
      @Schema(description = "전화번호")
      private String phone;
      @Schema(description = "주소")
      private String address;
      @Schema(description = "메모")
      private String memo;
      @Schema(description = "진도표")
      private List<ProgressItem> progress;

      public static Response fromEntity(Customers customers, Progress progress) {
        return Response.builder()
            .customerId(customers.getId())
            .photoUrl(customers.getPhotoUrl())
            .name(customers.getName())
            .gender(customers.getGender())
            .phone(customers.getPhone())
            .address(customers.getAddress())
            .memo(customers.getMemo())
            .progress(progress.getProgressList())
            .build();
      }
    }
}
