package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.ProgressRequest;
import java.util.List;

public class ProgressExtractor {

  public List<Long> extractProgressIds(List<UpdateCustomerDto.ProgressRequest> req) {
    return req.stream()
        .map(ProgressRequest::getProgressId)
        .toList();
  }

}
