package com.erp.erp.domain.reservations.service;

import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.business.ReservationsCreator;
import com.erp.erp.domain.reservations.business.ReservationsReader;
import com.erp.erp.domain.reservations.business.ReservationsUpdater;
import com.erp.erp.domain.reservations.common.dto.AddReservationsDto;
import com.erp.erp.domain.reservations.common.dto.GetDailyReservationsDto;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationsService {

  private final ReservationsReader reservationsReader;
  private final ReservationsCreator reservationsCreator;
  private final ReservationsUpdater reservationsUpdater;
  private final CustomersReader customersReader;


  public Reservations addReservations(AddReservationsDto.Request req, Customers customers) {
    Institutes institutes = customers.getInstitutes();

    Reservations reservations = Reservations.builder()
        .institutes(institutes)
        .customers(customers)
        .startTime(req.getStartTime())
        .endTime(req.getEndTime())
        .memo(req.getMemo())
        .build();

    return reservationsCreator.save(reservations);
  }

  public List<Reservations> getDailyReservations(LocalDate date, Institutes institutes) {
    return reservationsReader.findByInstitutesAndStartTimeOn(institutes, date);
  }



}
