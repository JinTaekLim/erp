package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
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
public class CustomerReader {

  private final CustomerRepository customerRepository;

  public Customer findById(Long customersId) {
    return customerRepository.findById(customersId).orElseThrow(NotFoundCustomerException::new);
  }

  public Page<Customer> findByInstitutesIdAndStatusActive(Institutes institutes, Pageable page) {
    return customerRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.ACTIVE, page);
  }

  public List<Customer> findByInstitutesIdAndStatusActive(Institutes institutes) {
    return customerRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.ACTIVE);
  }

  public List<Customer> findByInstitutesIdAndNameStartingWithAndStatusIn(Institutes institutes, String name) {
    List<CustomerStatus> status = Arrays.asList(CustomerStatus.ACTIVE, CustomerStatus.INACTIVE);
    return customerRepository.findByInstitutesIdAndNameStartingWithAndStatusIn(institutes.getId(), name, status);
  }

  public Page<Customer> findByInstitutesIdAndStatusInactive(Institutes institutes, Pageable page) {
    return customerRepository.findByInstitutesIdAndStatus(institutes.getId(), CustomerStatus.INACTIVE, page);
  }
}
