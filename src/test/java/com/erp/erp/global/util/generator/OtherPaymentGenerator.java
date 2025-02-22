package com.erp.erp.global.util.generator;

import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.util.List;

public class OtherPaymentGenerator extends EntityGenerator{

  public static List<OtherPayment> getList(Plan plan) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayment.class)
        .setNull("id")
        .set("plans", plan)
        .sampleList(randomInt);
  }
}
