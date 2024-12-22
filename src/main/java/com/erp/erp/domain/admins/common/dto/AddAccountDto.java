package com.erp.erp.domain.admins.common.dto;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddAccountDto {

  @Schema(name = "AddAccountDto_Request" , description = "계정 추가 요청")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @Schema(description = "매장 ID")
    @Positive
    private Long instituteId;

    @Schema(description = "아이디")
    @NotNull
    private String account;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;

    public Account toEntityWithInstitute(Institutes institutes) {
      return Account.builder().institutes(institutes).account(account).password(password).build();
    }

  }

  @Schema(name = "AddAccountDto_Response" , description = "계정 추가 반환")
  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{

    @Schema(description = "매장 ID")
    @NotNull
    private Long instituteId;

    @Schema(description = "아이디")
    @NotNull
    private String account;

    public static AddAccountDto.Response fromEntity(Account account) {
      return Response.builder()
          .instituteId(account.getInstitutes().getId())
          .account(account.getAccount())
          .build();
    }
  }


}
