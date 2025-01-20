package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import java.util.List;

public class ProgressGenerator extends EntityGenerator{

  public static List<Progress> get(Customer customer, int size) {
    return fixtureMonkey.giveMeBuilder(Progress.class)
        .setNull("id")
        .set("customer", customer)
        .sampleList(size);
  }
}
