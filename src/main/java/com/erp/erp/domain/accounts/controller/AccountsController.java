package com.erp.erp.domain.accounts.controller;

import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto;
import com.erp.erp.domain.accounts.service.AccountsService;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.auth.service.TokenService;
import com.erp.erp.global.error.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;
  private final TokenService tokenService;


  @PostMapping("/login")
  public ApiResult<TokenDto> login(@Valid @RequestBody AccountsLoginDto.Request request) {
    TokenDto response = tokenService.getToken();
    return ApiResult.success(response);


  }
}
