package com.erp.erp.domain.admin.common.mapper;

import com.erp.erp.domain.admin.common.dto.UpdatePlanCacheEvent;
import com.erp.erp.domain.admin.common.dto.UpdatePlanCacheMessageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminEventMapper {

  UpdatePlanCacheMessageDto updatePlanCacheEvent(UpdatePlanCacheEvent event);

}
