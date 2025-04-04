package com.erp.erp.domain.reservation.service;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.ProgressManger;
import com.erp.erp.domain.customer.business.ProgressReader;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.institute.business.InstituteValidator;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.business.ReservationDelete;
import com.erp.erp.domain.reservation.business.ReservationSender;
import com.erp.erp.domain.reservation.business.ReservationValidator;
import com.erp.erp.domain.reservation.business.ReservationCreator;
import com.erp.erp.domain.reservation.business.ReservationReader;
import com.erp.erp.domain.reservation.business.ReservationUpdater;
import com.erp.erp.domain.reservation.common.dto.*;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.mapper.ReservationMapper;
import com.erp.erp.global.util.TimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final ReservationMapper reservationMapper;
  private final ProgressReader progressReader;
  private final ProgressManger progressManger;
  private final ReservationSender reservationSender;


  public void sendAddReservationRequest(AddReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(req.getCustomerId(),
        institute.getId());

    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());

    LocalDateTime startTime = reservationValidator.validateReservationTime(req.getStartTime());
    LocalDateTime endTime = reservationValidator.validateReservationTime(req.getEndTime());
    reservationValidator.isEndTimeAfterStartTime(startTime, endTime);

    reservationSender.sendAddReservation(account, customer, req, startTime, endTime);
  }

  public void addReservations(Account account, Customer customer, AddReservationDto.Request req,
      LocalDateTime startTime, LocalDateTime endTime) {
    Institute institute = account.getInstitute();

    reservationValidator.isTimeSlotAvailable(institute, startTime, endTime);

    Reservation reservation = reservationMapper.dtoToEntity(
        req, institute, customer, String.valueOf(account.getId())
    );

    reservationCreator.save(reservation);
  }

  public List<GetDailyReservationDto.Response> getDailyReservations(LocalDate date) {
    Institute institute = authProvider.getCurrentInstitute();
    List<Reservation> reservations = reservationReader.findByInstitutesAndStartTimeOn(institute, date);
    return reservationMapper.entityToGetDailyReservationDtoResponse(reservations);
  }

  public List<GetDailyReservationDto.Response> getReservationByTime(LocalDateTime time) {
    Institute institute = authProvider.getCurrentInstitute();

    LocalDateTime startTime = TimeUtil.roundToNearestHalfHour(time);
    LocalDateTime endTime = startTime.plusMinutes(30);

    List<Reservation> reservations = reservationReader.findByInstitutesAndReservationTimeBetween(
        institute, startTime, endTime);
    return reservationMapper.entityToGetDailyReservationDtoResponse(reservations);
  }

  // note. 전달받은 시간 값 검증, 예약 가능 좌석인지 검증 필요
  @Transactional
  public UpdatedReservationDto.Response updateReservation(UpdatedReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();

    Reservation reservation = reservationReader.findByIdAndInstituteId(req.getReservationId(),
        institute.getId());

    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());
    reservationUpdater.updatedReservations(reservation, req, String.valueOf(account.getId()));

    List<Progress> progressList = progressManger.add(
        reservation.getCustomer(), req.getProgressList(), String.valueOf(account.getId())
    );

    return reservationMapper.entityToUpdatedReservationDtoResponse(reservation, progressList);
  }

  // note. 변경된 좌석에 예약이 존재하는지 검증 필요
  public UpdatedSeatNumberDto.Response updatedSeatNumber(UpdatedSeatNumberDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();

    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());
    Reservation reservation = reservationReader.findByIdAndInstituteId(req.getReservationId(),
        institute.getId());
    reservationUpdater.updateSeatNumber(
        reservation, req.getSeatNumber(), String.valueOf(account.getId())
    );

    return reservationMapper.entityToUpdatedSeatNumberDtoResponse(reservation);
  }


  public void deleteReservations(Long reservationId) {
    Institute institute = authProvider.getCurrentInstitute();
    Reservation reservation = reservationReader.findByIdAndInstituteId(reservationId,
        institute.getId());
    reservationDelete.delete(reservation);
  }

  public GetReservationCustomerDetailsDto.Response getReservationCustomerDetails(
      Long reservationsId) {
    Institute institute = authProvider.getCurrentInstitute();
    Reservation reservation = reservationReader.findByIdAndInstituteId(reservationsId,
        institute.getId());
    List<Progress> progressList = progressReader.findByCustomerId(reservation.getCustomer().getId());
    return reservationMapper.entityToGetReservationCustomerDetailsDtoResponse(reservation, progressList);
  }


}
