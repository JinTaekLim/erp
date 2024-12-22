package com.erp.erp.domain.admins.controller;


import com.erp.erp.domain.accounts.service.AccountsService;
import com.erp.erp.domain.admins.common.dto.AddAccountDto;
import com.erp.erp.domain.admins.common.dto.AddInstituteDto;
import com.erp.erp.domain.admins.common.dto.AddPlanDto;
import com.erp.erp.domain.institutes.service.InstitutesService;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.service.PlanService;
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
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "관리자용")
@RequiredArgsConstructor
@Slf4j
public class adminsController {

  private final PlanService planService;
  private final InstitutesService instituteService;
  private final AccountsService accountsService;


  @Operation(summary = "이용권 등록")
  @PostMapping("/addPlans")
  public ApiResult<AddPlanDto.Response> addPlans(
      @RequestBody @Valid AddPlanDto.Request request
  ) {
    Plan plan = planService.addPlans(request);

    AddPlanDto.Response response = AddPlanDto.Response.builder()
        .planType(plan.getPlanType())
        .licenseType(plan.getLicenseType())
        .name(plan.getName())
        .price(plan.getPrice())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "매장 등록")
  @PostMapping("/addInstitute")
  public ApiResult<AddInstituteDto.Response> addInstitute(
      @RequestBody @Valid AddInstituteDto.Request req
  ) {
    AddInstituteDto.Response response = instituteService.addInstitute(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "계정 추가")
  @PostMapping("/addAccount")
  public ApiResult<AddAccountDto.Response> addAccount(
      @RequestBody @Valid AddAccountDto.Request req
  ) {
    AddAccountDto.Response response = accountsService.addAccount(req);
    return ApiResult.success(response);
  }

}
