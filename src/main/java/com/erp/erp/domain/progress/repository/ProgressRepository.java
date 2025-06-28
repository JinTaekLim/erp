package com.erp.erp.domain.progress.repository;

import com.erp.erp.domain.progress.common.entity.Progress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

  void deleteAllByCustomerId(Long customerId);

  List<Progress> findByCustomerId(Long customerId);

  List<Progress> findAllByIdInAndCustomerId(List<Long> id, Long customerId);

  void deleteByReservationId(Long reservationId);
}
