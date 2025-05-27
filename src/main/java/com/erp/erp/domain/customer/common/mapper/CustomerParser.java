package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.projection.GetCustomersProjection;
import com.erp.erp.domain.reservation.business.ReservationCalculator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerParser {

  private final CustomerMapper customerMapper = new CustomerMapperImpl();
  private final ReservationCalculator reservationCalculator = new ReservationCalculator();

  public List<GetCustomerDto.Response> getCustomers(
      List<GetCustomersProjection.Customer> customers,
      List<GetCustomersProjection.Reservation> reservations
  ) {

    // CustomerID 를 기준으로 Reservation 을 구분
    Map<Long, List<GetCustomersProjection.Reservation>> reservationMap = reservations.stream()
        .collect(Collectors.groupingBy(GetCustomersProjection.Reservation::getCustomerId));


    // customers 와 reservations 를 병합 매핑
    return customers.stream().map(c -> {

      List<GetCustomersProjection.Reservation> customerReservations = reservationMap.getOrDefault(c.getCustomerId(), List.of());

      double usedTime = customerReservations.stream()
          .mapToDouble(r -> reservationCalculator.getUsedTime(r.getStartIndex(), r.getEndIndex()))
          .sum();

      int lateCount = customerReservations.stream()
          .mapToInt(r -> reservationCalculator.getLateCount(r.getAttendanceStatus()))
          .sum();

      int absenceCount = customerReservations.stream()
          .mapToInt(r -> reservationCalculator.getAbsenceCount(r.getAttendanceStatus()))
          .sum();

      int availablePeriod = c.getAvailablePeriod();
      int remainingPeriod = reservationCalculator.getRemainingPeriod(
          availablePeriod, c.getExpiredAt(), c.getRegistrationDate().toLocalDate()
      );

      double remainingTime = reservationCalculator.getRemainingTime(c.getAvailableTime(), usedTime);

      return customerMapper.getCustomers(
          c, usedTime, lateCount, absenceCount, remainingPeriod, remainingTime
      );

    }).toList();

  }

}
