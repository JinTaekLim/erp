package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface CustomerRepositoryCustom {

  List<Customer> findByInstituteIdAndNameStartingWithAndStatusIn(
      @Param("instituteId") Long instituteId, @Param("name") String name,
      @Param("statuses") List<CustomerStatus> statuses);

  void updateStatusById(@Param("customerId") Long customerId,
      @Param("newStatus") CustomerStatus newStatus);


}
