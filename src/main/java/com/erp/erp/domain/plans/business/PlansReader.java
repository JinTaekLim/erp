package com.erp.erp.domain.plans.business;

import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.common.exception.NotFoundPlansException;
import com.erp.erp.domain.plans.repository.PlansRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlansReader {

  private final PlansRepository plansRepository;

  public List<Plans> findAll() {
    return plansRepository.findAll();
  }

  public Plans findById(Long id) {
    return plansRepository.findById(id).orElseThrow(NotFoundPlansException::new);
  }
}
