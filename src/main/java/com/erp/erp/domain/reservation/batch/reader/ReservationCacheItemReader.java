package com.erp.erp.domain.reservation.batch.reader;

import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.institute.business.InstituteReader;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.business.ReservationCacheReader;
import com.erp.erp.domain.reservation.common.dto.ComparisonGetCustomerDto;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCacheItemReader implements ItemReader<ComparisonGetCustomerDto> {

  private final ReservationCacheReader reservationCacheReader;
  private final InstituteReader instituteReader;
  private final CustomerReader customerReader;

  private List<Institute> institutes;
  private int currentIndex = 0;


  @Override
  public ComparisonGetCustomerDto read() {

    if (institutes == null) {
      institutes = instituteReader.findAll();
    }

    if (currentIndex >= institutes.size()) {return null;}


    Institute institute = institutes.get(currentIndex++);
    Long instituteId = institute.getId();
    Long lastId = customerReader.findTopIdByInstituteId(instituteId) + 1;

    List<GetCustomerDto.Response> db = customerReader.findAllAfterLastId(instituteId, lastId, CustomerStatus.ACTIVE, 20);
    List<ReservationCache> cache = reservationCacheReader.findByInstituteId(instituteId);

    return new ComparisonGetCustomerDto(instituteId, db, cache);
  }

}
