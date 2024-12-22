package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationUpdater {
  private final ReservationRepository reservationRepository;

  public Reservation updatedReservations(
      Reservation reservation,
      LocalDateTime startTime,
      LocalDateTime endTime,
      String memo
  ) {
    reservation.updatedReservations(startTime,endTime,memo);
    return reservationRepository.save(reservation);
  }

  public Reservation updateSeatNumber(Reservation reservation, int seatNumber) {
    reservation.updatedSeat(seatNumber);
    return reservation;
  }
}
