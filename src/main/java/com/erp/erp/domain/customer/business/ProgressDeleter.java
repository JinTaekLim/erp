package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.repository.ProgressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressDeleter {

  private final ProgressRepository progressRepository;

  public void deleteAllByCustomerId(Long customerId) {
    progressRepository.deleteAllByCustomerId(customerId);
  }

  public void deleteAll(List<Progress> progressList) {
    progressRepository.deleteAll(progressList);
  }

  public void deleteAllById(List<Long> ids) {
    progressRepository.deleteAllById(ids);
  }

}
