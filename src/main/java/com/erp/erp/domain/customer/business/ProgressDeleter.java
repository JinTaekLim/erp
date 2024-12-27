package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressDeleter {

  private final ProgressRepository progressRepository;

  public void deleteAllByCustomerId(Long customerId) {
    progressRepository.deleteAllByCustomerId(customerId);
  }

}
