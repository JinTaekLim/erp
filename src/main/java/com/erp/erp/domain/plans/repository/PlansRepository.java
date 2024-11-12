package com.erp.erp.domain.plans.repository;

import com.erp.erp.domain.plans.common.entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlansRepository extends JpaRepository<Plans, Long> {

}
