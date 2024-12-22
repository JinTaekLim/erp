package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.repository.ProgressRepository;
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
