package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;

public class CustomerPhotoGenerator extends EntityGenerator {

  public static CustomerPhoto get(Customer customer) {
    return fixtureMonkey.giveMeBuilder(CustomerPhoto.class)
        .set("customer", customer)
        .sample();
  }

}
