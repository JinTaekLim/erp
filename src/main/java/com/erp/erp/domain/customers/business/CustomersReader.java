package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.exception.NotFoundCustomersException;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomersReader {

  private final CustomersRepository customersRepository;

  public Customers findById(Long customersId) {
    return customersRepository.findById(customersId).orElseThrow(NotFoundCustomersException::new);
  }

  public Page<Customers> findByInstitutesIdAndStatusTrue(Institutes institutes, Pageable page) {
    return customersRepository.findByInstitutesIdAndStatus(institutes.getId(), true, page);
  }

  public Page<Customers> findByInstitutesIdAndStatusFalse(Institutes institutes, Pageable page) {
    return customersRepository.findByInstitutesIdAndStatus(institutes.getId(), false, page);
  }
}
