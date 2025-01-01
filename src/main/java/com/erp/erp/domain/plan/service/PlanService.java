package com.erp.erp.domain.plan.service;

import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.mapper.PlanMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

  private final PlanReader planReader;
  private final PlanMapper planMapper;

  public List<GetPlanDto.Response> getPlans(LicenseType licenseType) {
    List<Plan> plans = planReader.findByLicensType(licenseType);
    return planMapper.entityToGetPlanResponseList(plans);
  }
}
