package com.erp.erp.domain.reservations.business;

import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationsUpdater {
  private final ReservationsRepository reservationsRepository;

  public Reservations reservationsUpdate(
      Reservations reservations,
      LocalDateTime startTime,
      LocalDateTime endTime,
      String memo
  ) {
    reservations.update(startTime,endTime,memo);
    return reservationsRepository.save(reservations);
  }
}
