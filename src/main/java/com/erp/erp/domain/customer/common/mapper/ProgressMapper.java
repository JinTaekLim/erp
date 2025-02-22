package com.erp.erp.domain.customer.common.mapper;


import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

  @Mapping(source = "createdId", target = "createdId")
  Progress dtoToEntity(ProgressDto.AddProgress progressResponse, Customer customer, String createdId);

  default List<Progress> addProgressToEntityList(List<ProgressDto.AddProgress> progressResponses, Customer customer, String createdId) {
    return progressResponses.stream()
        .map(progressResponse -> dtoToEntity(progressResponse, customer, createdId))
        .toList();
  }
}
