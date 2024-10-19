package com.erp.erp.domain.institutes.controller;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.service.AuthService;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.service.InstitutesService;
import com.erp.erp.global.error.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/institutes")
@RequiredArgsConstructor
@Slf4j
public class InstitutesController {

  private final InstitutesService institutesService;
  private final AuthService authService;

  @PostMapping("/updateTotalSpots")
  public ApiResult<UpdateTotalSpotsDto.Response> updateTotalSpots(
      @Valid @RequestBody UpdateTotalSpotsDto.Request req
  ) {

    Accounts accounts = authService.getAccountsInfo();
    Institutes institutes = accounts.getInstitutes();
    UpdateTotalSpotsDto.Response response = institutesService.updateTotalSpots(institutes, req);
    return ApiResult.success(response);



  }

}
