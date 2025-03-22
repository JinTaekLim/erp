package com.erp.erp.domain.reservation.batch.processor;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.reservation.common.dto.ComparisonGetCustomerDto;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.mapper.ReservationCacheMapper;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheProcessor implements ItemProcessor<ComparisonGetCustomerDto, List<ReservationCache>> {

  private final ReservationCacheMapper reservationCacheMapper;

  @Override
  public List<ReservationCache> process(ComparisonGetCustomerDto item) {
    List<GetCustomerDto.Response> db = item.getDb();
    List<ReservationCache> cache = item.getCache();
    Long instituteId = item.getInstituteId();

    if (dtoEqualsEntity(db, cache)) {return null;}

    return reservationCacheMapper.dtoToEntity(db, instituteId);
  }

  private boolean dtoEqualsEntity(List<GetCustomerDto.Response> db, List<ReservationCache> entities) {
    if (db.size() != entities.size()) return false;

    return IntStream.range(0, db.size())
        .allMatch(i -> {
          GetCustomerDto.Response dto = db.get(i);
          ReservationCache entity = entities.get(i);
          return dto.getAbsenceCount() == entity.getAbsenceCount() &&
              dto.getUsedTime() == entity.getUsedTime() &&
              dto.getLateCount() == entity.getLateCount() &&
              dto.getCustomerId().equals(entity.getCustomerId());
        });
  }

}
