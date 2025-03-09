package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.plan.common.entity.Plan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCustomerMessageDto {

  private Account account;
  private Plan plan;
  private AddCustomerDto.Request req;
  private byte[] file;

}
