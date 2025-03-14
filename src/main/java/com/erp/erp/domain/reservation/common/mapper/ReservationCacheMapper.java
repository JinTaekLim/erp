package com.erp.erp.domain.reservation.common.mapper;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationCacheMapper {

  @Mapping(target = "instituteId", source = "customer.institute.id")
  @Mapping(target = "customerId", source = "customer.id")
  ReservationCache customerToEntity(Customer customer);

}
