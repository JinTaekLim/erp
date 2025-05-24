package com.erp.erp.domain.customer.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerCache {

  private List<GetCustomerDto.Response> getCustomers;
  private long updatedTime;

}
