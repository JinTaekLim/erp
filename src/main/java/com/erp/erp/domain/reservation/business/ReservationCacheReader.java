package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.repository.ReservationCacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheReader {

  private final ReservationCacheRepository reservationCacheRepository;

  public List<ReservationCache> findByInstituteId(Long instituteId) {
    return reservationCacheRepository.findByInstituteId(instituteId);
  }

  public ReservationCache findByCustomerId(Long instituteId, Long customerId) {
    return reservationCacheRepository.findByCustomerId(instituteId, customerId)
        .orElse(null);
  }

}
