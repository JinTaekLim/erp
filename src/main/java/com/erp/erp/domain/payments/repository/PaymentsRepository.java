package com.erp.erp.domain.payments.repository;

import com.erp.erp.domain.payments.common.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {

}
