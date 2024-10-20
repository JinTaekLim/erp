package com.erp.erp.domain.reservations.business;

import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationsDelete {
  private final ReservationsRepository reservationsRepository;

  public void delete(Reservations reservations) {
    reservationsRepository.delete(reservations);
  }

}
