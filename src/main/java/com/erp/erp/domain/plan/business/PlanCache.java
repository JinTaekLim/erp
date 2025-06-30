package com.erp.erp.domain.plan.business;

import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class PlanCache {

  List<Plan> cache = new CopyOnWriteArrayList<>();

  public void updatePlans(List<Plan> plans) {
    cache.clear();
    cache.addAll(plans);
  }

  public List<Plan> getPlansByLicenseType(LicenseType type) {
    return cache.stream()
        .filter(i -> i.getLicenseType().equals(type))
        .toList();
  }
}
