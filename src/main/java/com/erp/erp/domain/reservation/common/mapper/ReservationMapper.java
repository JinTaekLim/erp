package com.erp.erp.domain.reservation.common.mapper;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetDailyReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetReservationCustomerDetailsDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedSeatNumberDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  @Mapping(target = "memo", source = "req.memo")
  @Mapping(target = "customer", source = "customer")
  Reservation dtoToEntity(AddReservationDto.Request req, Institute institute, Customer customer);

  @Mapping(target = "reservationId", source = "reservation.id")
  AddReservationDto.Response entityToAddReservaionResponse(Reservation reservation);

  List<GetDailyReservationDto.Response> entityToGetDailyReservationDtoResponse(List<Reservation> reservations);
  @Mapping(target = "reservationId", source = "reservation.id")
  @Mapping(target = "name", source = "reservation.customer.name")
  GetDailyReservationDto.Response entityToGetDailyReservationDtoResponse(Reservation reservation);

  @Mapping(target = "reservationId", source = "reservation.id")
  UpdatedReservationDto.Response entityToUpdatedReservationDtoResponse(Reservation reservation);

  @Mapping(target = "reservationId", source = "reservation.id")
  UpdatedSeatNumberDto.Response entityToUpdatedSeatNumberDtoResponse(Reservation reservation);

  @Mapping(target = "photoUrl", source = "reservation.customer.photoUrl")
  @Mapping(target = "name", source = "reservation.customer.name")
  @Mapping(target = "gender", source = "reservation.customer.gender")
  @Mapping(target = "phone", source = "reservation.customer.phone")
  @Mapping(target = "planName", source = "reservation.customer.planPayment.plan.name")
  @Mapping(target = "endDate", expression = "java(calculateEndDate(reservation))")
  @Mapping(target = "remainingTime", expression = "java(0)")
  @Mapping(target = "usedTime", expression = "java(0)")
  @Mapping(target = "memo", source = "reservation.customer.memo")
  @Mapping(target = "progress", expression = "java(null)")
  GetReservationCustomerDetailsDto.Response entityToGetReservationCustomerDetailsDtoResponse(Reservation reservation);

  default LocalDateTime calculateEndDate(Reservation reservation) {
    Customer customer = reservation.getCustomer();
    Plan plan = customer.getPlanPayment().getPlan();
    LocalDateTime registrationAt = customer.getPlanPayment().getRegistrationAt().withNano(0);
    int availablePeriod = plan.getAvailablePeriod();
    return registrationAt.plusDays(availablePeriod);
  }
}
