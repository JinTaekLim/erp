package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.institute.business.InstituteLock;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.NoAvailableSeatException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationValidator {

  private final ReservationReader reservationReader;
  private final PendingReservationReader pendingReservationReader;
  private final PendingReservationCreator pendingReservationCreator;
  private final InstituteLock instituteLock;

  public PendingReservationDto checkAndReserveTimeSlot(
      Institute institute, LocalDate day, int startIndex, int endIndex
  ) {
    Long instituteId = institute.getId();

    // DB 에서 가져온 예약을 인덱스(시간) 별로 나열함
    int[] slot = getSlot(institute, day, startIndex, endIndex);

    try {
      // 동시성 이슈를 위해 분산락 획득
      instituteLock.getLock(instituteId);
      addPendingReservation(slot, instituteId, day, startIndex, endIndex);

      checkAvailableSeat(slot, institute.getTotalSeat());

      // 예약 가능한 범위일때, 해당 데이터를 Redis 에 임시 저장
      return savePendingReservation(instituteId, day, startIndex, endIndex);

    } catch (InterruptedException e) {
      throw new RuntimeException("isTimeSlotAvailable", e);
    } finally {
      // 분산락 해제
      instituteLock.unLock(instituteId);
    }
  }

  public int[] getSlot(Institute institute, LocalDate day, int startIndex, int endIndex) {
    // DB 에 저장 되어있는 범위 내 예약을 가져옴
    List<Reservation> reservations = reservationReader.findReservationsWithinTimeRange(institute, day, startIndex, endIndex);

    // DB 에서 가져온 예약을 인덱스(시간) 별로 나열하고 반환
    int[] slot = new int[endIndex - startIndex];

    reservations.forEach(r ->
        IntStream.range(r.getStartIndex(), r.getEndIndex())
            .forEach(i -> slot[i - startIndex]++)
    );

    return slot;
  }

  public void addPendingReservation(int[] slot, Long instituteId, LocalDate day, int startIndex, int endIndex) {
    // Redis 에 임시 저장 되어있는 예약 목록 조회
    List<PendingReservationDto> pendingReservations = pendingReservationReader.findByInstituteId(instituteId);

    // 예약 목록 중 day 가 동일하고 startIndex 와 endIndex 범위 내의 데이터만을 slot 배열에 나열
    pendingReservations.stream()
        .filter(r -> LocalDate.parse(r.getDay()).equals(day))
        .filter(r -> r.getStartIndex() >= startIndex)
        .filter(r -> r.getEndIndex() <= endIndex)
        .forEach(r -> {
          IntStream.range(r.getStartIndex(), r.getEndIndex())
              .forEach(i -> slot[i - startIndex]++);
        });

  }

  public PendingReservationDto getPendingReservation(LocalDate day, int startIndex, int endIndex) {
    return PendingReservationDto.builder()
        .day(String.valueOf(day))
        .startIndex(startIndex)
        .endIndex(endIndex)
        .build();
  }

  public PendingReservationDto savePendingReservation(Long instituteId, LocalDate day, int startIndex, int endIndex) {
    PendingReservationDto dto = getPendingReservation(day, startIndex, endIndex);
    pendingReservationCreator.save(instituteId, dto);
    return dto;
  }

  public void checkAvailableSeat(int[] slot, int totalSeat) {
    // 예약이 가능한 좌석이 없으면 예외를 던짐
    if (IntStream.of(slot).anyMatch(s -> s >= totalSeat)) {
      throw new NoAvailableSeatException();
    }
  }

  public void checkStartTimeBeforeEndTime(int startIndex, int endIndex) {
    if (startIndex >= endIndex) {
      throw new InvalidReservationTimeException();
    }
  }
}
