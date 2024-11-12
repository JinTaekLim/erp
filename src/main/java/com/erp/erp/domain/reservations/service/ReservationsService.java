package com.erp.erp.domain.reservations.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.business.InstitutesValidator;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.business.ReservationsDelete;
import com.erp.erp.domain.reservations.business.ReservationsValidator;
import com.erp.erp.domain.reservations.business.ReservationsCreator;
import com.erp.erp.domain.reservations.business.ReservationsReader;
import com.erp.erp.domain.reservations.business.ReservationsUpdater;
import com.erp.erp.domain.reservations.common.dto.AddReservationsDto;
import com.erp.erp.domain.reservations.common.dto.DeleteReservationsDto;
import com.erp.erp.domain.reservations.common.dto.UpdatedReservationsDto;
import com.erp.erp.domain.reservations.common.dto.UpdatedSeatNumberDto;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.global.util.TimeUtil;
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

  private final AuthProvider authProvider;
  private final InstitutesValidator institutesValidator;
  private final ReservationsReader reservationsReader;
  private final ReservationsCreator reservationsCreator;
  private final ReservationsUpdater reservationsUpdater;
  private final ReservationsDelete reservationsDelete;
  private final ReservationsValidator reservationsValidator;
  private final CustomersReader customersReader;
  public Reservations findById(Long reservationsId) {
    return reservationsReader.findById(reservationsId);
  }

  public Reservations addReservations(AddReservationsDto.Request req) {
    Institutes institutes = authProvider.getCurrentInstitutes();
    Customers customers = customersReader.findById(req.getCustomersId());
    institutesValidator.validateCustomerBelongsToInstitute(institutes,customers);

    LocalDateTime startTime = req.getStartTime();
    LocalDateTime endTime = req.getEndTime();

    reservationsValidator.isTimeSlotAvailable(institutes, startTime, endTime);

    Reservations reservations = Reservations.builder()
        .institutes(institutes)
        .customers(customers)
        .startTime(startTime)
        .endTime(endTime)
        .memo(req.getMemo())
        .build();

    return reservationsCreator.save(reservations);
  }

  public List<Reservations> getDailyReservations(LocalDate date) {
    Institutes institutes = authProvider.getCurrentInstitutes();
    return reservationsReader.findByInstitutesAndStartTimeOn(institutes, date);
  }

  public List<Reservations> getReservationByTime(LocalDateTime time) {
    Institutes institutes = authProvider.getCurrentInstitutes();

    LocalDateTime startTime = TimeUtil.roundToNearestHalfHour(time);
    LocalDateTime endTime = startTime.plusMinutes(30);

    return reservationsReader.findByInstitutesAndReservationTimeBetween(
        institutes,
        startTime,
        endTime
    );
  }

  public Reservations updateReservation(UpdatedReservationsDto.Request req) {

    Institutes institutes = authProvider.getCurrentInstitutes();
    long reservationsId = req.getReservationsId();
    Reservations reservations = reservationsReader.findById(reservationsId);
    Customers customers = reservations.getCustomers();
    institutesValidator.validateCustomerBelongsToInstitute(institutes, customers);

    LocalDateTime startTime = req.getStartTime();
    LocalDateTime endTime = req.getEndTime();
    String memo = req.getMemo();

    return reservationsUpdater.updatedReservations(reservations, startTime, endTime, memo);
  }




  public void deleteReservations(DeleteReservationsDto.Request req) {
    Institutes institutes = authProvider.getCurrentInstitutes();
    long reservationsId = req.getReservationsId();
    Reservations reservations = reservationsReader.findById(reservationsId);
    Customers customers = reservations.getCustomers();
    institutesValidator.validateCustomerBelongsToInstitute(institutes, customers);
    reservationsDelete.delete(reservations);
  }



}
