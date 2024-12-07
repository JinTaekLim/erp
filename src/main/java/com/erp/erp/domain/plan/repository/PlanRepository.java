package com.erp.erp.domain.plan.repository;

import com.erp.erp.domain.plan.common.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {

}
