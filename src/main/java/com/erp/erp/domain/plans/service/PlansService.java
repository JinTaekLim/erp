package com.erp.erp.domain.plans.service;

import com.erp.erp.domain.plans.business.PlansCreator;
import com.erp.erp.domain.plans.business.PlansReader;
import com.erp.erp.domain.plans.common.dto.AddPlansDto;
import com.erp.erp.domain.plans.common.entity.Plans;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlansService {

  private final PlansCreator plansCreator;
  private final PlansReader plansReader;

  public Plans addPlans(AddPlansDto.Request request) {

    Plans plans = Plans.builder()
        .licenseType(request.getLicenseType())
        .name(request.getName())
        .price(request.getPrice())
        .availableTime(request.getAvailableTime())
        .availablePeriod(request.getAvailablePeriod())
        .build();

    return plansCreator.save(plans);
  }

  public List<Plans> getAllMemberships() {
    return plansReader.findAll();
  }

  public Plans findById(Long id) {
    return plansReader.findById(id);
  }
}
