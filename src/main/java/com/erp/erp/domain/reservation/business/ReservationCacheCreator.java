package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.repository.ReservationCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheCreator {

  private final ReservationCacheRepository reservationCacheRepository;

  public void save(ReservationCache reservationCache) {
    reservationCacheRepository.save(reservationCache);
  }
}
