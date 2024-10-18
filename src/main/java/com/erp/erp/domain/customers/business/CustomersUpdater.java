package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomersUpdater {

  private final CustomersRepository customersRepository;


  public void updateStatus(Long customersId, boolean status) {
    customersRepository.updateStatusById(customersId, status);
  }

}
