package com.erp.erp.domain.institutes.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.business.InstitutesUpdater;
import com.erp.erp.domain.institutes.business.InstitutesValidator;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstitutesService {

  private final AuthProvider authProvider;
  private final InstitutesUpdater institutesUpdater;
  private final InstitutesValidator institutesValidator;
  private final CustomersReader customersReader;

  public UpdateTotalSpotsDto.Response updateTotalSpots(UpdateTotalSpotsDto.Request req) {
    Institutes institutes = authProvider.getCurrentInstitutes();
    int num = req.getNum();
    institutesUpdater.updateSpotsNumber(institutes, num);

    return UpdateTotalSpotsDto.Response.builder()
        .id(institutes.getId())
        .name(institutes.getName())
        .num(institutes.getTotalSpots())
        .build();
  }
}
