package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerUpdater {

  private final CustomerRepository customerRepository;


  public void updateStatus(Long customersId, CustomerStatus status) {
    customerRepository.updateStatusById(customersId, status);
  }
}
