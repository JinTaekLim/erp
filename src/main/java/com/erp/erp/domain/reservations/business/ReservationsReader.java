package com.erp.erp.domain.reservations.business;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.common.exception.NotFoundReservationException;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationsReader {
  private final ReservationsRepository reservationsRepository;

  public List<Reservations> findByInstitutesAndStartTimeOn(Institutes institutes, LocalDate date) {
    List<Reservations> reservationsList = reservationsRepository.findByInstitutesAndStartTimeOn(
        institutes,
        date
    );
    if (reservationsList.isEmpty()) throw new NotFoundReservationException();
    return reservationsList;
  }

  public Reservations findById(Long reservationsId) {
    return reservationsRepository.findById(reservationsId)
        .orElseThrow(NotFoundReservationException::new);
  }


}
