package com.erp.erp.domain.admins.controller;


import com.erp.erp.domain.plans.common.dto.AddPlansDto;
import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.service.PlansService;
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

  private final PlansService plansService;


  @PostMapping("/addPlans")
  public ApiResult<AddPlansDto.Response> addPlans(
      @RequestBody @Valid AddPlansDto.Request request
  ) {
    Plans plans = plansService.addPlans(request);

    AddPlansDto.Response response = AddPlansDto.Response.builder()
        .licenseType(plans.getLicenseType())
        .name(plans.getName())
        .price(plans.getPrice())
        .build();

    return ApiResult.success(response);
  }

}
