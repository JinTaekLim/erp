package com.erp.erp.domain.plan.service;

import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

  private final PlanReader planReader;

  public List<Plan> getPlans(LicenseType licenseType) {
    return planReader.findByLicensType(licenseType);
  }

  public Plan findById(Long id) {
    return planReader.findById(id);
  }
}
