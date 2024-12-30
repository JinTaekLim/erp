package com.erp.erp.global.util.generator;

import com.erp.erp.domain.plan.common.entity.Plan;

public class PlanGenerator extends EntityGenerator{

  public static Plan get() {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .setNull("id")
        .sample();
  }
}
