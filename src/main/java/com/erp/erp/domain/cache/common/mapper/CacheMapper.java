package com.erp.erp.domain.cache.common.mapper;

import com.erp.erp.domain.customer.common.dto.GetCustomerCache;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CacheMapper {

  GetCustomerCache toGetCustomerCache(List<GetCustomerDto.Response> getCustomers, long updatedTime);
}