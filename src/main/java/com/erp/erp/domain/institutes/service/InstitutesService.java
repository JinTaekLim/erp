package com.erp.erp.domain.institutes.service;

import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.business.InstitutesUpdater;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.common.exception.InstituteNotFoundInCustomerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstitutesService {

  private final InstitutesUpdater institutesUpdater;
  private final CustomersReader customersReader;

  public UpdateTotalSpotsDto.Response updateTotalSpots(
      Institutes institutes,
      UpdateTotalSpotsDto.Request req
  ) {

    int num = req.getNum();
    institutesUpdater.updateSpotsNumber(institutes, num);

    return UpdateTotalSpotsDto.Response.builder()
        .id(institutes.getId())
        .name(institutes.getName())
        .num(institutes.getTotalSpots())
        .build();
  }

  public Customers validateCustomerBelongsToInstitute(Institutes institutes , Long customersId) {
    Customers customers = customersReader.findById(customersId);
    Institutes comparativeInstitutes = customers.getInstitutes();
    if (institutes == comparativeInstitutes) {return customers;}
    else {throw new InstituteNotFoundInCustomerException();}
  }
}
