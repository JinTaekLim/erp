package com.erp.erp.domain.plan.controller;


import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.service.PlanService;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
@Tag(name = "plan", description = "이용권 관리")
@RequiredArgsConstructor
@Slf4j
public class PlanController {

  private final PlanService planService;

  @Operation(summary = "이용권 조회")
  @GetMapping("/getPlans/{licenseType}")
  public ApiResult<List<GetPlanDto.Response>> getPlans(@PathVariable("licenseType") LicenseType licenseType) {
    List<GetPlanDto.Response> response = planService.getPlans(licenseType);
    return ApiResult.success(response);
  }

}
