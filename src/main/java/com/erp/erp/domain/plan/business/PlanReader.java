package com.erp.erp.domain.plan.business;

import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.exception.NotFoundPlanException;
import com.erp.erp.domain.plan.repository.PlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanReader {

  private final PlanRepository planRepository;

  public List<Plan> findAll() {
    return planRepository.findAll();
  }

  public Plan findById(Long id) {
    return planRepository.findById(id).orElseThrow(NotFoundPlanException::new);
  }
}
