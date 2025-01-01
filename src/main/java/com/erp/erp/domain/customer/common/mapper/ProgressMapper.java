package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.ProgressResponse;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

  Progress dtoToEntity(ProgressResponse progressResponse, Customer customer);

  default List<Progress> dtoToEntityList(List<ProgressResponse> progressResponses, Customer customer) {
    return progressResponses.stream()
        .map(progressResponse -> dtoToEntity(progressResponse, customer))
        .toList();
  }
}
