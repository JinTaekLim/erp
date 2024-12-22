package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCreator {
  private final ReservationRepository reservationRepository;

  public Reservation save(Reservation reservation) {
    return reservationRepository.save(reservation);
  }
}
