package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.NotFoundReservationException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import java.time.LocalDate;
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
    return reservationRepository.findByInstituteAndStartDate(institute, date);
  }

  public Reservation findByIdAndInstituteId(Long reservationId, Long instituteId) {
    return reservationRepository.findByIdAndInstituteId(reservationId, instituteId)
        .orElseThrow(NotFoundReservationException::new);
  }

  public List<Reservation> findReservationsWithinTimeRange(Institute institute, LocalDate day, int startIndex, int endIndex) {
    return reservationRepository.findReservationsWithinTimeRange(institute,day,startIndex,endIndex);
  }

}
