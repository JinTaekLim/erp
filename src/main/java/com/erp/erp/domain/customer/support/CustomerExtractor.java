package com.erp.erp.domain.customer.support;

import com.erp.erp.domain.customer.common.projection.GetCustomersProjection;
import java.util.List;

public class CustomerExtractor {

  public List<Long> getIds(List<GetCustomersProjection.Customer> customers) {
    return customers.stream()
        .map(GetCustomersProjection.Customer::getCustomerId)
        .toList();
  }

}
