package com.erp.erp.domain.customers.repository;

import com.erp.erp.domain.customers.common.entity.CustomerStatus;
import com.erp.erp.domain.customers.common.entity.Customers;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

  Page<Customers> findByInstitutesIdAndStatus(Long institutesId, CustomerStatus status, Pageable pageable);
  List<Customers> findByInstitutesIdAndStatus(Long institutesId, CustomerStatus status);


  @Modifying
  @Transactional
  @Query("UPDATE Customers c SET c.status = :newStatus WHERE c.id = :customerId")
  void updateStatusById(Long customerId, CustomerStatus newStatus);


}
