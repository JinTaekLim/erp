package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Page<Customer> findByInstitutesIdAndStatus(Long institutesId, CustomerStatus status, Pageable pageable);
  List<Customer> findByInstitutesIdAndStatus(Long institutesId, CustomerStatus status);

  @Query(value = "SELECT * FROM customers c WHERE c.institutes_id = :institutesId " +
      "AND c.name LIKE CONCAT(:name, '%') " +
      "AND c.status IN (:statuses)",
      nativeQuery = true)
  List<Customer> findByInstitutesIdAndNameStartingWithAndStatusIn(
      Long institutesId, String name, List<CustomerStatus> statuses);




  @Modifying
  @Transactional
  @Query("UPDATE Customer c SET c.status = :newStatus WHERE c.id = :customerId")
  void updateStatusById(Long customerId, CustomerStatus newStatus);


}
