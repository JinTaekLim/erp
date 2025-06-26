package com.erp.erp.domain.progress.common.mapper;


import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.reservation.common.entity.Reservation;
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
  @Mapping(target = "reservation", source = "reservation")
  @Mapping(target = "date", source = "reservation.reservationDate")
  @Mapping(target = "createdId", source = "createdId")
  Progress toEntity(Customer customer, Reservation reservation, Double usedTime, String createdId);

}
