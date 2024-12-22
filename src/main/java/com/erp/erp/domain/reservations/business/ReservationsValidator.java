package com.erp.erp.domain.reservations.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservations.common.exception.NoAvailableSpotsException;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationsValidator {

  private final ReservationsRepository reservationsRepository;

  public void isTimeSlotAvailable(
      Institute institute,
      LocalDateTime startTime,
      LocalDateTime endTime
  ) {
    List<Reservations> reservationsList = reservationsRepository.findByInstitutesAndTimeRange(
        institute,
        startTime,
        endTime
    );

    int totalSpots = institute.getTotalSeat();
    int slot = calculate30MinSlots(startTime, endTime);
    long[] slotOccupancy = new long[slot];

    for (Reservations reservation : reservationsList) {
      int reservationStartIndex = getReservationStartIndex(startTime, reservation.getStartTime());
      int reservationEndIndex = calculate30MinSlots(reservation.getStartTime(), reservation.getEndTime());

      checkSlotOccupancy(slotOccupancy, reservationStartIndex, reservationEndIndex, totalSpots);
    }
  }

  private int getReservationStartIndex(LocalDateTime startTime, LocalDateTime reservationStartTime){
    return startTime.equals(reservationStartTime) ? 0 : calculate30MinSlots(startTime, reservationStartTime);
  }

  private void checkSlotOccupancy(long[] slotOccupancy, int startIndex, int endIndex, int totalSpots) {
    for (int slotIndex = startIndex; slotIndex < endIndex; slotIndex++) {
      if (slotOccupancy[slotIndex] >= totalSpots) {throw new NoAvailableSpotsException();}
      slotOccupancy[slotIndex]++;
    }
  }

  public int calculate30MinSlots(LocalDateTime startTime, LocalDateTime endTime) {
    long totalMinutes = getMinutesBetween(startTime, endTime);
    return (int) (totalMinutes / 30);
  }

  public long getMinutesBetween(LocalDateTime startTime, LocalDateTime endTime) {
    if (!startTime.isBefore(endTime)) {
      throw new InvalidReservationTimeException();
    }
    return ChronoUnit.MINUTES.between(startTime, endTime);
  }
}
