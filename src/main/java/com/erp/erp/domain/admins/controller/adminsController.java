package com.erp.erp.domain.admins.controller;


import com.erp.erp.domain.plan.common.dto.AddPlanDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.service.PlanService;
import com.erp.erp.global.error.ApiResult;
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


  @PostMapping("/addPlans")
  public ApiResult<AddPlanDto.Response> addPlans(
      @RequestBody @Valid AddPlanDto.Request request
  ) {
    Plan plan = planService.addPlans(request);

    AddPlanDto.Response response = AddPlanDto.Response.builder()
        .licenseType(plan.getLicenseType())
        .name(plan.getName())
        .price(plan.getPrice())
        .build();

    return ApiResult.success(response);
  }

}
