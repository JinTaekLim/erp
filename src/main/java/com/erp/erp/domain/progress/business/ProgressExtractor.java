package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.ProgressRequest;
import com.erp.erp.domain.reservation.common.dto.UpdateReservationDto;
import java.util.List;

public class ProgressExtractor {

  public List<Long> extractProgressIds(List<UpdateCustomerDto.ProgressRequest> req) {
    return req.stream()
        .map(ProgressRequest::getProgressId)
        .toList();
  }

  public List<Long> extractUpdateReservationToProgressIds(List<UpdateReservationDto.ProgressRequest> req) {
    return req.stream()
        .map(UpdateReservationDto.ProgressRequest::getProgressId)
        .toList();
  }

}
