package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPhotoRepository extends JpaRepository<CustomerPhoto, Long> {

  Optional<CustomerPhoto> findByCustomer(Customer customer);

  void deleteByCustomer(Customer customer);
}
