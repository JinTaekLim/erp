package com.erp.erp.domain.progress.common.mapper;


import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.progress.common.entity.Progress;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

//  @Mapping(source = "createdId", target = "createdId")
//  Progress dtoToEntity(ProgressDto.Request req, Customer customer, String createdId);
//
//  default List<Progress> addProgressToEntityList(List<ProgressDto.Request> req, Customer customer, String createdId) {
//    return req.stream()
//        .map(progressResponse -> dtoToEntity(progressResponse, customer, createdId))
//        .toList();
//  }


  @Mapping(target = "customer", source = "customer")
  Progress toEntity(Customer customer, LocalDate date, Double usedTime, String createdId);

}
