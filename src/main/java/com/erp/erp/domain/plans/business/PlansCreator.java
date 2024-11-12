package com.erp.erp.domain.plans.business;

import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.repository.PlansRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlansCreator {

  private final PlansRepository plansRepository;

  public Plans save(Plans plans) {
    return plansRepository.save(plans);
  }
}
