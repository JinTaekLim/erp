package com.erp.erp.domain.admin.common.mapper;

import com.erp.erp.domain.admin.common.dto.UpdatePlanCacheEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

  UpdatePlanCacheEvent updatePlanCacheEvent(Long adminId);

}
