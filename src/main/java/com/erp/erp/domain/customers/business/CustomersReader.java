package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.exception.NotFoundCustomersException;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomersReader {

  private final CustomersRepository customersRepository;

  public Customers findById(Long customersId) {
    return customersRepository.findById(customersId).orElseThrow(NotFoundCustomersException::new);
  }

}
