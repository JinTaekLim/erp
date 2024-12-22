package com.erp.erp.domain.reservations.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.business.InstituteValidator;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservations.business.ReservationsDelete;
import com.erp.erp.domain.reservations.business.ReservationsValidator;
import com.erp.erp.domain.reservations.business.ReservationsCreator;
import com.erp.erp.domain.reservations.business.ReservationsReader;
import com.erp.erp.domain.reservations.business.ReservationsUpdater;
import com.erp.erp.domain.reservations.common.dto.*;
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
  private final InstituteValidator instituteValidator;
  private final ReservationsReader reservationsReader;
  private final ReservationsCreator reservationsCreator;
  private final ReservationsUpdater reservationsUpdater;
  private final ReservationsDelete reservationsDelete;
  private final ReservationsValidator reservationsValidator;
  private final CustomerReader customerReader;


  public Reservations addReservations(AddReservationsDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Customer customer = customerReader.findById(req.getCustomersId());
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    LocalDateTime startTime = req.getStartTime();
    LocalDateTime endTime = req.getEndTime();

    reservationsValidator.isTimeSlotAvailable(institute, startTime, endTime);

    Reservations reservations = Reservations.builder()
        .institute(institute)
        .customer(customer)
        .startTime(startTime)
        .endTime(endTime)
        .memo(req.getMemo())
        .build();

    return reservationsCreator.save(reservations);
  }

  public List<Reservations> getDailyReservations(LocalDate date) {
    Institute institute = authProvider.getCurrentInstitute();
    return reservationsReader.findByInstitutesAndStartTimeOn(institute, date);
  }

  public List<Reservations> getReservationByTime(LocalDateTime time) {
    Institute institute = authProvider.getCurrentInstitute();

    LocalDateTime startTime = TimeUtil.roundToNearestHalfHour(time);
    LocalDateTime endTime = startTime.plusMinutes(30);

    return reservationsReader.findByInstitutesAndReservationTimeBetween(
        institute,
        startTime,
        endTime
    );
  }

  public Reservations updateReservation(UpdatedReservationsDto.Request req) {

    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationsId();
    Reservations reservations = reservationsReader.findById(reservationsId);
    Customer customer = reservations.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    LocalDateTime startTime = req.getStartTime();
    LocalDateTime endTime = req.getEndTime();
    String memo = req.getMemo();

    return reservationsUpdater.updatedReservations(reservations, startTime, endTime, memo);
  }

  public Reservations updatedSeatNumber(UpdatedSeatNumberDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationsId();
    Reservations reservations = reservationsReader.findById(reservationsId);
    Customer customer = reservations.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    return reservationsUpdater.updateSeatNumber(reservations, req.getSeatNumber());
  }


  public void deleteReservations(DeleteReservationsDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationsId();
    Reservations reservations = reservationsReader.findById(reservationsId);
    Customer customer = reservations.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);
    reservationsDelete.delete(reservations);
  }

  public GetReservationCustomerDetailsDto.Response getReservationsForCurrentInstitute(Long reservationsId) {
    Institute institute = authProvider.getCurrentInstitute();
    Reservations reservations = reservationsReader.findById(reservationsId);
    instituteValidator.validateReservationBelongsToInstitute(institute, reservations);
    return GetReservationCustomerDetailsDto.Response.fromEntity(reservations);
  }


}
