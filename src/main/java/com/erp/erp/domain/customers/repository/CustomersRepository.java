package com.erp.erp.domain.customers.repository;

import com.erp.erp.domain.customers.common.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomersRepository extends JpaRepository<Customers, Long> {


  @Modifying
  @Transactional
  @Query("UPDATE Customers c SET c.status = :newStatus WHERE c.id = :customerId")
  void updateStatusById(Long customerId, Boolean newStatus);


}
