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
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.business.ReservationDelete;
import com.erp.erp.domain.reservation.business.ReservationSender;
import com.erp.erp.domain.reservation.business.ReservationValidator;
import com.erp.erp.domain.reservation.business.ReservationCreator;
import com.erp.erp.domain.reservation.business.ReservationReader;
import com.erp.erp.domain.reservation.business.ReservationUpdater;
import com.erp.erp.domain.reservation.common.dto.*;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.mapper.ReservationMapper;
import java.time.LocalDate;
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
  private final ReservationCacheManager reservationCacheManager;


  public void sendAddReservationRequest(AddReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(req.getCustomerId(),
        institute.getId());

    // 영업 시간 내의 예약인지 검사
    instituteValidator.validateOperatingHours(institute, req.getStartIndex(), req.getEndIndex());

    // 예약이 가능한 좌석인지 검사
    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());

    // 시작 시간 보다 종료 시간이 같거나 작은지 검사
    reservationValidator.checkStartTimeBeforeEndTime(req.getStartIndex(), req.getEndIndex());

    reservationSender.sendAddReservation(account, customer, req);
  }

  @Transactional
  public void addReservations(Account account, Customer customer, AddReservationDto.Request req) {
    Institute institute = account.getInstitute();

    reservationValidator.isTimeSlotAvailable(institute, req.getReservationDate(), req.getStartIndex(), req.getEndIndex(), req.getSeatNumber());

    Reservation reservation = reservationMapper.dtoToEntity(
        req, institute, customer, String.valueOf(account.getId())
    );

    reservationCreator.save(reservation);
    reservationCacheManager.update(reservation);
  }

  public List<GetDailyReservationDto.Response> getDailyReservations(LocalDate date) {
    Institute institute = authProvider.getCurrentInstitute();
    List<Reservation> reservations = reservationReader.findByInstitutesAndStartTimeOn(institute, date);
    return reservationMapper.entityToGetDailyReservationDtoResponse(reservations);
  }

//  public List<GetDailyReservationDto.Response> getReservationByTime(LocalDate day, Long startIndex, Long endIndex) {
//    Institute institute = authProvider.getCurrentInstitute();
//
//    List<Reservation> reservations = reservationReader.findByInstitutesAndReservationTimeBetween(
//        institute, day, startIndex, endIndex);
//    return reservationMapper.entityToGetDailyReservationDtoResponse(reservations);
//  }

  // note. 전달받은 시간 값 검증, 예약 가능 좌석인지 검증 필요
  @Transactional
  public UpdatedReservationDto.Response updateReservation(UpdatedReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();

    Reservation oldReservation = reservationReader.findByIdAndInstituteId(req.getReservationId(),
        institute.getId());

    reservationCacheManager.update(oldReservation, req);

    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());
    Reservation reservation = reservationUpdater.updatedReservations(oldReservation, req, String.valueOf(account.getId()));

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
