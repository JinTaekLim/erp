package com.erp.erp.domain.customer.common.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateCustomersCacheEvent {

  private Long instituteId;
  private LocalDateTime time;

}
