package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.NoAvailableSeatException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationValidator {

  private final ReservationReader reservationReader;

  public void isTimeSlotAvailable(
      Institute institute, LocalDate day, int startIndex, int endIndex, int seatNum
  ) {
    List<Reservation> reservations = reservationReader.findReservationsWithinTimeRange(institute, day, startIndex, endIndex);

    int reservedSeats = reservations.stream()
        .filter(r -> r.getSeatNumber() == seatNum)
        .mapToInt(Reservation::getSeatNumber)
        .sum();

    if (reservedSeats >= institute.getTotalSeat()) {
      throw new NoAvailableSeatException();
    }
  }

  public void checkStartTimeBeforeEndTime(int startIndex, int endIndex) {
    if (startIndex > endIndex) {
      throw new InvalidReservationTimeException();
    }
  }
}
