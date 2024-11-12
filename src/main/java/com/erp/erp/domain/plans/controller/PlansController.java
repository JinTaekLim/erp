package com.erp.erp.domain.plans.controller;


import com.erp.erp.domain.plans.common.dto.GetPlansDto;
import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.service.PlansService;
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
public class PlansController {

  private final PlansService plansService;

  @Operation(summary = "이용권 전체 조회")
  @GetMapping("/getAllMemberships")
  public ApiResult<List<GetPlansDto.Response>> getAllMemberships() {

    List<Plans> plansList = plansService.getAllMemberships();

    List<GetPlansDto.Response> responses = plansList.stream()
        .map(membership -> GetPlansDto.Response.builder()
            .id(membership.getId())
            .name(membership.getName())
            .price(membership.getPrice())
            .build()
        ).toList();

    return ApiResult.success(responses);
  }

}
