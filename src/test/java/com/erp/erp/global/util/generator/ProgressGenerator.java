package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.util.List;

public class ProgressGenerator extends EntityGenerator{

  public static List<Progress> get(Customer customer, List<Reservation> reservations) {

    return reservations.stream().map(reservation -> {
      return fixtureMonkey.giveMeBuilder(Progress.class)
          .setNull("id")
          .set("customer", customer)
          .set("reservation", reservation)
          .sample();
    }).toList();
  }

  public static Progress get(Customer customer, Reservation reservation) {
    return fixtureMonkey.giveMeBuilder(Progress.class)
        .setNull("id")
        .set("customer", customer)
        .set("reservation", reservation)
        .sample();
  }
}
