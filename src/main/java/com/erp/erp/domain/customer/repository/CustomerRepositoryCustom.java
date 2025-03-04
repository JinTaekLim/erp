package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryCustom {

  List<Customer> findByInstituteIdAndNameStartingWithAndStatusIn(
      Long instituteId, String name, List<CustomerStatus> statuses);

  void updateStatusById(Long customerId, CustomerStatus newStatus, String updatedId);

  Optional<Long> findTopIdByInstituteId(Long instituteId);

  List<Customer> findAllByInstituteBeforeIdAndStatus(
      Long instituteId, Long lastId, CustomerStatus status, int size
  );

  List<UpdateCustomerExpiredAtDto> findCustomersCreatedAtOnDaysAgo(int days);

  void updateExpiredAt(List<UpdateCustomerExpiredAtDto.Request> req);

  List<Long> findIdsCreatedAtBeforeDaysAgo(LocalDate date);

  void updatePhotoUrl(Customer customer);
}
