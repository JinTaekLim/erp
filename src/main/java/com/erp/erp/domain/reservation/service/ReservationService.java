package com.erp.erp.domain.reservation.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.business.InstituteValidator;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.business.ReservationDelete;
import com.erp.erp.domain.reservation.business.ReservationValidator;
import com.erp.erp.domain.reservation.business.ReservationCreator;
import com.erp.erp.domain.reservation.business.ReservationReader;
import com.erp.erp.domain.reservation.business.ReservationUpdater;
import com.erp.erp.domain.reservation.common.dto.*;
import com.erp.erp.domain.reservation.common.entity.Reservation;
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
public class ReservationService {

  private final AuthProvider authProvider;
  private final InstituteValidator instituteValidator;
  private final ReservationReader reservationReader;
  private final ReservationCreator reservationCreator;
  private final ReservationUpdater reservationUpdater;
  private final ReservationDelete reservationDelete;
  private final ReservationValidator reservationValidator;
  private final CustomerReader customerReader;


  public Reservation addReservations(AddReservationDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Customer customer = customerReader.findById(req.getCustomerId());
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    LocalDateTime startTime = reservationValidator.validateReservationTime(req.getStartTime());
    LocalDateTime endTime = reservationValidator.validateReservationTime(req.getEndTime());

    reservationValidator.isTimeSlotAvailable(institute, startTime, endTime);

    Reservation reservation = Reservation.builder()
        .institute(institute)
        .customer(customer)
        .startTime(startTime)
        .endTime(endTime)
        .memo(req.getMemo())
        .build();

    return reservationCreator.save(reservation);
  }

  public List<Reservation> getDailyReservations(LocalDate date) {
    Institute institute = authProvider.getCurrentInstitute();
    return reservationReader.findByInstitutesAndStartTimeOn(institute, date);
  }

  public List<Reservation> getReservationByTime(LocalDateTime time) {
    Institute institute = authProvider.getCurrentInstitute();

    LocalDateTime startTime = TimeUtil.roundToNearestHalfHour(time);
    LocalDateTime endTime = startTime.plusMinutes(30);

    return reservationReader.findByInstitutesAndReservationTimeBetween(
        institute,
        startTime,
        endTime
    );
  }

  public Reservation updateReservation(UpdatedReservationDto.Request req) {

    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationId();
    Reservation reservation = reservationReader.findById(reservationsId);
    Customer customer = reservation.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    LocalDateTime startTime = req.getStartTime();
    LocalDateTime endTime = req.getEndTime();
    String memo = req.getMemo();

    return reservationUpdater.updatedReservations(reservation, startTime, endTime, memo);
  }

  public Reservation updatedSeatNumber(UpdatedSeatNumberDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationId();
    Reservation reservation = reservationReader.findById(reservationsId);
    Customer customer = reservation.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);

    return reservationUpdater.updateSeatNumber(reservation, req.getSeatNumber());
  }


  public void deleteReservations(DeleteReservationDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    long reservationsId = req.getReservationId();
    Reservation reservation = reservationReader.findById(reservationsId);
    Customer customer = reservation.getCustomer();
    instituteValidator.validateCustomerBelongsToInstitute(institute, customer);
    reservationDelete.delete(reservation);
  }

  public GetReservationCustomerDetailsDto.Response getReservationsForCurrentInstitute(Long reservationsId) {
    Institute institute = authProvider.getCurrentInstitute();
    Reservation reservation = reservationReader.findById(reservationsId);
    instituteValidator.validateReservationBelongsToInstitute(institute, reservation);
    return GetReservationCustomerDetailsDto.Response.fromEntity(reservation);
  }


}
