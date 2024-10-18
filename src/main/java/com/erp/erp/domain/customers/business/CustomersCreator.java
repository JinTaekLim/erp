package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomersCreator {

  private final CustomersRepository customersRepository;

  public Customers save(Customers customers) {
    return customersRepository.save(customers);
  }
}
