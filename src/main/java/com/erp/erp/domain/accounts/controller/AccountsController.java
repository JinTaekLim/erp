package com.erp.erp.domain.accounts.controller;

import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto;
import com.erp.erp.domain.accounts.service.AccountsService;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "account", description = "계정 관리")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;


  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ApiResult<TokenDto> login(@Valid @RequestBody AccountsLoginDto.Request request) {
    TokenDto response = accountsService.login(request);
    return ApiResult.success(response);
  }
}
