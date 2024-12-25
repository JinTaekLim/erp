package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.NotFoundReservationException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationReader {
  private final ReservationRepository reservationRepository;

  public List<Reservation> findByInstitutesAndStartTimeOn(Institute institute, LocalDate date) {
    List<Reservation> reservationList = reservationRepository.findByInstituteAndStartTimeOn(
        institute,
        date
    );
    if (reservationList.isEmpty()) throw new NotFoundReservationException();
    return reservationList;
  }

  public List<Reservation> findByInstitutesAndReservationTimeBetween(
      Institute institute,
      LocalDateTime startTime,
      LocalDateTime endTime
  ){
    return reservationRepository.findByInstituteWithOverlappingTimeRange(
        institute,
        startTime,
        endTime
    );
  };

  public Reservation findById(Long reservationsId) {
    return reservationRepository.findById(reservationsId)
        .orElseThrow(NotFoundReservationException::new);
  }


}
