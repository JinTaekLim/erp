package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Progress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

  void deleteAllByCustomerId(Long customerId);

  List<Progress> findByCustomerId(Long customerId);
}
