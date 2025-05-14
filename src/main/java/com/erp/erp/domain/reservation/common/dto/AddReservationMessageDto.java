package com.erp.erp.domain.reservation.common.dto;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.entity.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddReservationMessageDto {
  private Account account;
  private Customer customer;
  private PendingReservationDto pendingReservation;
  private AddReservationDto.Request req;
}
