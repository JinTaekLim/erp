package com.erp.erp.domain.admin.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdatePlanCacheEvent {

  private final Long adminId;

}
