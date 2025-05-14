package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationUpdater {

  public Reservation updatedReservations(
      Reservation reservation,
      UpdatedReservationDto.Request req,
      String updatedId
  ) {
    reservation.updatedReservations(
        req.getReservationDate(), req.getStartIndex(), req.getEndIndex(),
        req.getMemo(), req.getSeatNumber(),
        req.getAttendanceStatus(), updatedId);
    return reservation;
  }

  public Reservation updateSeatNumber(Reservation reservation, int seatNumber, String updatedId) {
    reservation.updatedSeat(seatNumber, updatedId);
    return reservation;
  }
}
