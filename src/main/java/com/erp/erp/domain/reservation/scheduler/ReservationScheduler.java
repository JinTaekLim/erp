package com.erp.erp.domain.reservation.scheduler;

import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.institute.business.InstituteReader;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.mapper.ReservationCacheMapper;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

  private final InstituteReader instituteReader;
  private final CustomerReader customerReader;
  private final ReservationCacheManager reservationCacheManager;
  private final ReservationCacheMapper reservationCacheMapper;

  // 매정각 DB 데이터와 캐시 데이터가 동일한지 확인 후 조치
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateGetCustomer() {
    List<Institute> institute = instituteReader.findAll();
    List<Long> ids = institute.stream().map(Institute::getId).toList();

    for (Long id : ids) {
      Long lastId = customerReader.findTopIdByInstituteId(id) + 1;

      List<GetCustomerDto.Response> db = customerReader.findAllAfterLastId(id, lastId,
          CustomerStatus.ACTIVE, 20);
      List<ReservationCache> cache = reservationCacheManager.findByInstituteId(id);

      if (dtoEqualsEntity(db, cache)) {continue;}

      List<ReservationCache> reservationCaches = reservationCacheMapper.dtoToEntity(db, id);
      reservationCacheManager.update(id, reservationCaches);
    }

  }

  private boolean dtoEqualsEntity(List<GetCustomerDto.Response> dtos, List<ReservationCache> entities) {
    if (dtos.size() != entities.size()) return false;

    return IntStream.range(0, dtos.size())
        .allMatch(i -> {
          GetCustomerDto.Response dto = dtos.get(i);
          ReservationCache entity = entities.get(i);
          return dto.getAbsenceCount() == entity.getAbsenceCount() &&
              dto.getUsedTime() == entity.getUsedTime() &&
              dto.getLateCount() == entity.getLateCount() &&
              dto.getCustomerId().equals(entity.getCustomerId());
        });
  }


}
