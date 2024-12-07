package com.erp.erp.domain.plan.business;

import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanCreator {

  private final PlanRepository planRepository;

  public Plan save(Plan plan) {
    return planRepository.save(plan);
  }
}
