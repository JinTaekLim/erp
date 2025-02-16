package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryCustom {

  List<Customer> findByInstituteIdAndNameStartingWithAndStatusIn(
      Long instituteId, String name, List<CustomerStatus> statuses);

  void updateStatusById(Long customerId, CustomerStatus newStatus);

  Optional<Long> findTopIdByInstituteId(Long instituteId);

  List<Customer> findAllAfterLastId(Long instituteId, Long lastId, int size);

}
