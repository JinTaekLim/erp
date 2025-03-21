package com.erp.erp.domain.reservation.common.mapper;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationCacheMapper {

  @Mapping(target = "instituteId", source = "customer.institute.id")
  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "reservationId", ignore = true)
  @Mapping(target = "usedTime", ignore = true)
  @Mapping(target = "lateCount", ignore = true)
  @Mapping(target = "absenceCount", ignore = true)
  ReservationCache customerToEntity(Customer customer);

  @Mapping(target = "reservationId", ignore = true)
  @Mapping(target = "instituteId", source = "instituteId")
  @Mapping(source = "dto.customerId", target = "customerId")
  @Mapping(source = "dto.usedTime", target = "usedTime")
  @Mapping(source = "dto.lateCount", target = "lateCount")
  @Mapping(source = "dto.absenceCount", target = "absenceCount")
  ReservationCache dtoToEntity(GetCustomerDto.Response dto, Long instituteId);

  default List<ReservationCache> dtoToEntity(List<GetCustomerDto.Response> dtos, Long instituteId) {
    return dtos.stream()
        .map(dto -> dtoToEntity(dto, instituteId))
        .toList();
  }

}
