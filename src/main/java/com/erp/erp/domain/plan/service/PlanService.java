package com.erp.erp.domain.plan.service;

import com.erp.erp.domain.plan.business.PlanCache;
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
  private final PlanCache planCache;

  public List<GetPlanDto.Response> getPlans(LicenseType licenseType) {

    List<Plan> plans = planCache.getPlansByLicenseType(licenseType);

    // 캐시에 이용권 정보가 없을 경우, DB 에서 조회 후 캐시 업데이트
    if (plans.isEmpty()) {
      plans = planReader.findByLicensType(licenseType);
      planCache.updatePlans(plans);
    }

     return planMapper.entityToGetPlanResponseList(plans);
  }

  public void updatePlanCache() {
    List<Plan> plans = planReader.findAll();
    planCache.updatePlans(plans);
  }
}
