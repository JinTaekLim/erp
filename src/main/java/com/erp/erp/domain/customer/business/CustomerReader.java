package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public List<Customer> findByInstitutesIdAndStatusActive(Institute institute) {
    return customerRepository.findByInstituteIdAndStatus(institute.getId(), CustomerStatus.ACTIVE);
  }

  public List<Customer> findByInstitutesIdAndNameStartingWithAndStatusIn(Institute institute, String name) {
    List<CustomerStatus> status = Arrays.asList(CustomerStatus.ACTIVE, CustomerStatus.INACTIVE);
    return customerRepository.findByInstituteIdAndNameStartingWithAndStatusIn(institute.getId(), name, status);
  }

  public Long findTopIdByInstituteId(Long instituteId) {
    return customerRepository.findTopIdByInstituteId(instituteId)
        .orElse(0L);
  }

  public List<Customer> findAllAfterLastId(Long instituteId, Long lastId, CustomerStatus status, int size) {
    return customerRepository.findAllByInstituteBeforeIdAndStatus(instituteId, lastId, status, size);
  }

  public List<UpdateCustomerExpiredAtDto> findCustomersCreatedAtOnDaysAgo(int beforeDay) {
    return customerRepository.findCustomersCreatedAtOnDaysAgo(beforeDay);
  }

  public List<Long> findIdsCreatedAtBeforeDaysAgo(LocalDate date) {
    return customerRepository.findIdsCreatedAtBeforeDaysAgo(date);
  }
}
