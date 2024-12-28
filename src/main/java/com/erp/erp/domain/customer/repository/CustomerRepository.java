package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

  Optional<Customer> findByIdAndInstituteId(Long id, Long institutesId);

  Page<Customer> findByInstituteIdAndStatus(Long instituteId, CustomerStatus status, Pageable pageable);
  List<Customer> findByInstituteIdAndStatus(Long instituteId, CustomerStatus status);
}
