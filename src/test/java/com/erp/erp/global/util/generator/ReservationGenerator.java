package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDateTime;

public class ReservationGenerator extends EntityGenerator{

  public static Reservation get(Customer customer, Institute institute,
      LocalDateTime startTime, LocalDateTime endTime) {
    return Reservation.builder()
        .customer(customer)
        .institute(institute)
        .startTime(startTime)
        .endTime(endTime)
        .build();
  }
}
