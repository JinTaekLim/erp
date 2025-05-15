package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationUpdater {

//  public Reservation updatedReservations(
//      Reservation reservation, LocalDate date, int startIndex, int endIndex, String memo,
//      int seatNumber, AttendanceStatus attendanceStatus, Long updatedId
//  ) {
//    reservation.updatedReservations(
//        date, startIndex, endIndex, memo, seatNumber, attendanceStatus, updatedId.toString()
//    );
//    return reservation;
//  }

  public Reservation updateSeatNumber(Reservation reservation, int seatNumber, String updatedId) {
    reservation.updatedSeat(seatNumber, updatedId);
    return reservation;
  }
}
