package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto.Response;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ComparisonGetCustomerDto {

  private Long instituteId;
  private List<Response> db;
  private List<ReservationCache> cache;

}
