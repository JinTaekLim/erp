package com.erp.erp.domain.account.controller;

import com.erp.erp.domain.account.common.dto.AccountLoginDto;
import com.erp.erp.domain.account.service.AccountService;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.global.annotation.authentication.PermitAll;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@Tag(name = "account", description = "계정 관리")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private final AccountService accountService;


  @Operation(summary = "로그인")
  @PostMapping("/login")
  @PermitAll
  public ApiResult<TokenDto> login(@Valid @RequestBody AccountLoginDto.Request request) {
    TokenDto response = accountService.login(request);
    return ApiResult.success(response);
  }

  @Operation(summary = "토큰 재발급")
  @PostMapping("/reissueToken")
  @PermitAll
  public ApiResult<TokenDto> reissueToken(@RequestParam("refreshToken") String refreshToken) {
    TokenDto response = accountService.reissueToken(refreshToken);
    return ApiResult.success(response);
  }
}
