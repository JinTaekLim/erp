package com.erp.erp.domain.plan.service;

import com.erp.erp.domain.plan.business.PlanCreator;
import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.dto.AddPlanDto;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

  private final PlanCreator planCreator;
  private final PlanReader planReader;

  public Plan addPlans(AddPlanDto.Request request) {

    Plan plan = Plan.builder()
        .licenseType(request.getLicenseType())
        .name(request.getName())
        .price(request.getPrice())
        .availableTime(request.getAvailableTime())
        .availablePeriod(request.getAvailablePeriod())
        .build();

    return planCreator.save(plan);
  }

  public List<Plan> getAllPlans() {
    return planReader.findAll();
  }

  public Plan findById(Long id) {
    return planReader.findById(id);
  }
}
