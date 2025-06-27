package com.erp.erp.domain.reservation.service;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.GetCustomerCacheManager;
import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheEvent;
import com.erp.erp.domain.institute.business.InstituteLock;
import com.erp.erp.domain.progress.business.ProgressCreator;
import com.erp.erp.domain.progress.business.ProgressExtractor;
import com.erp.erp.domain.progress.business.ProgressReader;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.progress.business.ProgressUpdater;
import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.institute.business.InstituteValidator;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.progress.common.mapper.ProgressMapper;
import com.erp.erp.domain.reservation.business.PushUpdateReservationEvent;
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.business.ReservationCalculator;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

  private final ApplicationEventPublisher applicationEventPublisher;
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
  private final ReservationCacheManager reservationCacheManager;
  private final InstituteLock instituteLock;
  private final ProgressCreator progressCreator;
  private final ProgressMapper progressMapper;
  private final GetCustomerCacheManager getCustomerCacheManager;


  private final ProgressExtractor progressExtractor = new ProgressExtractor();
  private final ReservationCalculator reservationCalculator = new ReservationCalculator();
  private final ProgressUpdater progressUpdater;
  private final ReservationSender reservationSender;


  @Transactional
  public void addReservationRequest(AddReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(
        req.getCustomerId(), institute.getId()
    );

    String accountId = account.getId().toString();
    Long instituteId = institute.getId();

    // 요청 값 검증
    reservationValidator.validateRequest(
        institute, req.getStartIndex(), req.getEndIndex(), req.getSeatNumber()
    );

    Reservation reservation = reservationMapper.dtoToEntity(
        req, institute, customer, accountId
    );

    // 분산락 획득 후 작업 종료시 반환
    instituteLock.executeWithLock(instituteId, () -> {

      // 요청 시간 내 모든 예약 조회
      List<Reservation> reservations = reservationReader.findReservationsWithinTimeRange(
          institute, req.getReservationDate(), req.getStartIndex(), req.getEndIndex()
      );

      // 예약 가능한 좌석인지 검증
      reservationValidator.checkAvailableSeat(reservations, institute.getTotalSeat());

      // 예약 저장
      reservationCreator.save(reservation);
    });


    double usedTime = reservationCalculator.getUsedTime(req.getStartIndex(), req.getEndIndex());
    Progress progress = progressMapper.toEntity(customer, reservation, usedTime, accountId);
    progressCreator.save(progress);

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent updateCache = reservationMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(updateCache);

    // 트랜잭션 종료 이후 예약 변동 이벤트 메세지 큐 발행
    PushUpdateReservationEvent updateReservation = reservationMapper.toPushUpdateReservationEvent(instituteId);
    applicationEventPublisher.publishEvent(updateReservation);

  }

  @Transactional
  public void updateReservation(UpdateReservationDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();

    Long accountId = account.getId();
    Long instituteId = institute.getId();

    // 요청 값 검증
    reservationValidator.validateRequest(
        institute, req.getStartIndex(), req.getEndIndex(), req.getSeatNumber()
    );

    // 기존 예약 조회
    Reservation oldReservation = reservationReader.findByIdAndInstituteId(
        req.getReservationId(), institute.getId()
    );

    Reservation newReservation = oldReservation.updatedReservations(
        req.getReservationDate(), req.getStartIndex(), req.getEndIndex(), req.getMemo(),
        req.getSeatNumber(), req.getAttendanceStatus(), accountId.toString()
    );
    Customer customer = newReservation.getCustomer();

    // 분산락 획득 후 작업 종료시 반환
    instituteLock.executeWithLock(instituteId, () -> {

      // 요청 시간 내 모든 예약 조회
      List<Reservation> reservations = reservationReader.findReservationsWithinTimeRange(
          institute,
          req.getReservationDate(),
          req.getStartIndex(),
          req.getEndIndex()
      );

      // 예약 가능한 좌석인지 검증
      reservationValidator.checkAvailableSeat(reservations, institute.getTotalSeat());

      // 예약 수정
      reservationCreator.save(newReservation);
    });

    // 진도표 검증
    List<Long> ids = progressExtractor.extractUpdateReservationToProgressIds(req.getProgressList());
    progressReader.findByIdAndCustomerId(ids, customer.getId());

    // 진도표 저장
    progressUpdater.updateReservationProgress(req.getProgressList(), accountId);

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent updateCache = reservationMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(updateCache);

    // 트랜잭션 종료 이후 예약 변동 이벤트 메세지 큐 발행
    PushUpdateReservationEvent updateReservation = reservationMapper.toPushUpdateReservationEvent(instituteId);
    applicationEventPublisher.publishEvent(updateReservation);

  }

  public List<GetDailyReservationDto.Response> getDailyReservations(LocalDate date) {
    Institute institute = authProvider.getCurrentInstitute();
    List<Reservation> reservations = reservationReader.findByInstitutesAndStartTimeOn(institute, date);
    return reservationMapper.entityToGetDailyReservationDtoResponse(reservations);
  }


  // note. 변경된 좌석에 예약이 존재하는지 검증 필요
  @Transactional
  public UpdatedSeatNumberDto.Response updatedSeatNumber(UpdatedSeatNumberDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Long instituteId = institute.getId();

    instituteValidator.isValidSeatNumber(institute, req.getSeatNumber());
    Reservation reservation = reservationReader.findByIdAndInstituteId(req.getReservationId(),
        instituteId);
    reservationUpdater.updateSeatNumber(
        reservation, req.getSeatNumber(), String.valueOf(account.getId())
    );

    // 트랜잭션 종료 이후 예약 변동 이벤트 메세지 큐 발행
    PushUpdateReservationEvent updateReservation = reservationMapper.toPushUpdateReservationEvent(instituteId);
    applicationEventPublisher.publishEvent(updateReservation);

    return reservationMapper.entityToUpdatedSeatNumberDtoResponse(reservation);
  }


  @Transactional
  public void deleteReservations(Long reservationId) {
    Long instituteId = authProvider.getCurrentInstituteId();

    Reservation reservation = reservationReader.findByIdAndInstituteId(reservationId, instituteId);
    reservationDelete.delete(reservation);

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent updateCache = reservationMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(updateCache);

    // 트랜잭션 종료 이후 예약 변동 이벤트 메세지 큐 발행
    PushUpdateReservationEvent updateReservation = reservationMapper.toPushUpdateReservationEvent(instituteId);
    applicationEventPublisher.publishEvent(updateReservation);
  }

  public GetReservationCustomerDetailsDto.Response getReservationCustomerDetails(
      Long reservationsId) {
    Institute institute = authProvider.getCurrentInstitute();
    Reservation reservation = reservationReader.findByIdAndInstituteId(reservationsId,
        institute.getId());
    List<Progress> progressList = progressReader.findByCustomerIdAndDesc(reservation.getCustomer().getId());
    return reservationMapper.entityToGetReservationCustomerDetailsDtoResponse(reservation, progressList);
  }


}
