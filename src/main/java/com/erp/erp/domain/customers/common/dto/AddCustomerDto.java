package com.erp.erp.domain.customers.common.dto;

import com.erp.erp.domain.customers.common.entity.Gender;
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

//    @Schema(description = "프로필 사진")
//    private MultipartFile file;
  }


  @Schema(name = "AddCustomerDto_Response" , description = "회원 등록 반환")
  @Getter
  @Builder
  public static class Response {

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
  }
}
