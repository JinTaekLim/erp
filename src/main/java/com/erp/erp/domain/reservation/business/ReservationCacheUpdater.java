package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.repository.ReservationCacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheUpdater {

  private final ReservationCacheRepository reservationCacheRepository;

  public void update(ReservationCache reservationCache) {
    reservationCacheRepository.update(reservationCache);
  }

  public void update(Long instituteId, List<ReservationCache> reservationCacheList) {
    reservationCacheRepository.update(instituteId, reservationCacheList);
  }

}
