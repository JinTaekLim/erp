package com.erp.erp.global.util.generator;

import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;

public class PlanPaymentGenerator extends EntityGenerator{

  public static PlanPayment get(Plan plan) {
    return fixtureMonkey.giveMeBuilder(PlanPayment.class)
        .setNull("id")
        .set("plan", plan)
        .sample();
  }
}
