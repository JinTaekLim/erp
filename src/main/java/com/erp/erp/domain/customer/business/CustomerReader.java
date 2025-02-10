package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
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

  public Customer findByIdAndInstituteId(Long customerId, Long instituteId) {
    return customerRepository.findByIdAndInstituteId(customerId, instituteId)
        .orElseThrow(NotFoundCustomerException::new);
  }

  public Page<Customer> findByInstitutesIdAndStatusActive(Institute institute, Pageable page) {
    return customerRepository.findByInstituteIdAndStatus(institute.getId(), CustomerStatus.ACTIVE, page);
  }

  public List<Customer> findByInstitutesIdAndStatusActive(Institute institute) {
    return customerRepository.findByInstituteIdAndStatus(institute.getId(), CustomerStatus.ACTIVE);
  }

  public List<Customer> findByInstitutesIdAndNameStartingWithAndStatusIn(Institute institute, String name) {
    List<CustomerStatus> status = Arrays.asList(CustomerStatus.ACTIVE, CustomerStatus.INACTIVE);
    return customerRepository.findByInstituteIdAndNameStartingWithAndStatusIn(institute.getId(), name, status);
  }

  public Page<Customer> findByInstitutesIdAndStatusInactive(Institute institute, Pageable page) {
    return customerRepository.findByInstituteIdAndStatus(institute.getId(), CustomerStatus.INACTIVE, page);
  }
}
