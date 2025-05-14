package com.erp.erp.domain.reservation.batch.writer;

import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheWriter implements ItemWriter<List<ReservationCache>> {

  private final ReservationCacheManager reservationCacheManager;

  @Override
  public void write(Chunk<? extends List<ReservationCache>> chunk) {
    for (List<ReservationCache> reservationCache : chunk) {
      Long instituteId = reservationCache.get(0).getInstituteId();
      reservationCacheManager.updateAllInstituteCache(instituteId, reservationCache);
    }
  }
}
