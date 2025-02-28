package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.NoAvailableSeatException;
import com.erp.erp.domain.reservation.common.exception.TimeNotOnHalfHourException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationValidator {

  private final ReservationRepository reservationRepository;

  public LocalDateTime validateReservationTime(LocalDateTime time) {
    if (time.getMinute() % 30 != 0) {
      throw new TimeNotOnHalfHourException();
    }
    return time.withSecond(0).withNano(0);
  }


  public void isTimeSlotAvailable(
      Institute institute,
      LocalDateTime startTime,
      LocalDateTime endTime
  ) {
    List<Reservation> reservationList = reservationRepository.findByInstituteAndTimeRange(
        institute,
        startTime,
        endTime
    );

    int totalSpots = institute.getTotalSeat();
    int slot = calculate30MinSlots(startTime, endTime);
    long[] slotOccupancy = new long[slot];

    for (Reservation reservation : reservationList) {
      int startIndex = getReservationStartIndex(startTime, reservation.getStartTime());
      int endIndex = getReservationEndIndex(slot, startIndex, reservation.getStartTime(),
          reservation.getEndTime());
      checkSlotOccupancy(slotOccupancy, startIndex, endIndex, totalSpots);
    }
  }

  private int getReservationStartIndex(LocalDateTime startTime,
      LocalDateTime reservationStartTime) {
    return startTime.equals(reservationStartTime) ? 0
        : calculate30MinSlots(startTime, reservationStartTime);
  }

  private int getReservationEndIndex(int slotSize, int startIndex, LocalDateTime startTime,
      LocalDateTime endTime) {
    int endIndex = startIndex + calculate30MinSlots(startTime, endTime);
    return Math.min(endIndex, slotSize);
  }

  private void checkSlotOccupancy(long[] slotOccupancy, int startIndex, int endIndex,
      int totalSpots) {
    for (int slotIndex = startIndex; slotIndex < endIndex; slotIndex++) {
      if (slotOccupancy[slotIndex] >= totalSpots-1) {
        throw new NoAvailableSeatException();
      }
      slotOccupancy[slotIndex]++;
    }
  }

  public int calculate30MinSlots(LocalDateTime startTime, LocalDateTime endTime) {
    long totalMinutes = getMinutesBetween(startTime, endTime);
    return (int) (totalMinutes / 30);
  }

  public long getMinutesBetween(LocalDateTime startTime, LocalDateTime endTime) {
    isEndTimeAfterStartTime(startTime, endTime);
    return ChronoUnit.MINUTES.between(startTime, endTime);
  }

  public void isEndTimeAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
    if (!startTime.isBefore(endTime)) {
      throw new InvalidReservationTimeException();
    }
  }
}
