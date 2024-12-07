package com.erp.erp.domain.plan.controller;


import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.service.PlanService;
import com.erp.erp.global.error.ApiResult;
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
  public ApiResult<List<GetPlanDto.Response>> getPlans(@PathVariable LicenseType licenseType) {
    List<Plan> planList = planService.getPlans(licenseType);

    List<GetPlanDto.Response> responses = planList.stream()
        .map(plan -> GetPlanDto.Response.builder()
            .id(plan.getId())
            .licenseType(plan.getLicenseType())
            .name(plan.getName())
            .price(plan.getPrice())
            .build()
        ).toList();

    return ApiResult.success(responses);
  }

}
