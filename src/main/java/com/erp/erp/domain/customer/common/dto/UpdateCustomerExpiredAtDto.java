package com.erp.erp.domain.customer.common.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UpdateCustomerExpiredAtDto {

  private Long id;
  private LocalDate firstReservationDate;
  private int availablePeriod;

  @Getter
  @Builder
  public static class Request {
    private Long customerId;
    private LocalDate expiredAt;
  }

}
