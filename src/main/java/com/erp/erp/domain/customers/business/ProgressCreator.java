package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.Progress;
import com.erp.erp.domain.customers.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressCreator {

  private final ProgressRepository progressRepository;

  public void save(Progress progress) {
    progressRepository.save(progress);
  }

}
