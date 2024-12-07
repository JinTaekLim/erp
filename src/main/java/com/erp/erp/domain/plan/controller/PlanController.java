package com.erp.erp.domain.plan.controller;


import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.service.PlanService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@Tag(name = "plans", description = "이용권 관리")
@RequiredArgsConstructor
@Slf4j
public class PlanController {

  private final PlanService planService;

  @Operation(summary = "이용권 전체 조회")
  @GetMapping("/getAllPlans")
  public ApiResult<List<GetPlanDto.Response>> getAllPlans() {

    List<Plan> planList = planService.getAllPlans();

    List<GetPlansDto.Response> responses = plansList.stream()
        .map(membership -> GetPlansDto.Response.builder()
            .id(membership.getId())
            .name(membership.getName())
            .price(membership.getPrice())
    List<GetPlanDto.Response> responses = planList.stream()
        .map(plan -> GetPlanDto.Response.builder()
            .id(plan.getId())
            .name(plan.getName())
            .price(plan.getPrice())
            .build()
        ).toList();

    return ApiResult.success(responses);
  }

}
