package com.erp.erp.domain.customers.business;

import com.erp.erp.domain.customers.common.entity.CustomerStatus;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.exception.NotFoundCustomersException;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import java.util.Arrays;
import java.util.List;
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

  public Page<Customers> findByInstitutesIdAndStatusActive(Institutes institutes, Pageable page) {
    return customersRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.ACTIVE, page);
  }

  public List<Customers> findByInstitutesIdAndStatusActive(Institutes institutes) {
    return customersRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.ACTIVE);
  }

  public List<Customers> findByInstitutesIdAndNameStartingWithAndStatusIn(Institutes institutes, String name) {
    List<CustomerStatus> status = Arrays.asList(CustomerStatus.ACTIVE, CustomerStatus.INACTIVE);
    return customersRepository.findByInstitutesIdAndNameStartingWithAndStatusIn(institutes.getId(), name, status);
  }

  public Page<Customers> findByInstitutesIdAndStatusInactive(Institutes institutes, Pageable page) {
    return customersRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.INACTIVE, page);
  }
}
