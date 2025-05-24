package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheEvent;
import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheMessageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerEventMapper {

  UpdateCustomersCacheMessageDto toUpdateCustomersCacheMessageDto(UpdateCustomersCacheEvent event);
}
