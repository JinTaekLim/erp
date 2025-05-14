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

  public void updateCustomerReservation(ReservationCache reservationCache) {
    reservationCacheRepository.updateCustomerReservation(reservationCache);
  }

  public void updateAllInstituteCache(Long instituteId, List<ReservationCache> reservationCacheList) {
    reservationCacheRepository.updateAllInstituteCache(instituteId, reservationCacheList);
  }

  public void updateCacheExcludingCustomer(
      Long instituteId, Long customerId, List<ReservationCache> reservationCacheList
  ) {
    reservationCacheList = reservationCacheList.stream()
        .filter(reservationCache -> !reservationCache.getCustomerId().equals(customerId))
        .toList();

    reservationCacheRepository.updateAllInstituteCache(instituteId, reservationCacheList);
  }

}
