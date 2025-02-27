package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
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
      UpdatedReservationDto.Request req,
      String updatedId
  ) {
    reservation.updatedReservations(
        req.getStartTime(), req.getEndTime(), req.getMemo(), req.getSeatNumber(),
        req.getAttendanceStatus(), updatedId);
    return reservationRepository.save(reservation);
  }

  public Reservation updateSeatNumber(Reservation reservation, int seatNumber, String updatedId) {
    reservation.updatedSeat(seatNumber, updatedId);
    return reservation;
  }
}
