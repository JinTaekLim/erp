package com.erp.erp.domain.customer.common.mapper;


import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

  Progress dtoToEntity(ProgressDto.AddProgress progressResponse, Customer customer);

  default List<Progress> addProgressToEntityList(List<ProgressDto.AddProgress> progressResponses, Customer customer) {
    return progressResponses.stream()
        .map(progressResponse -> dtoToEntity(progressResponse, customer))
        .toList();
  }
}
