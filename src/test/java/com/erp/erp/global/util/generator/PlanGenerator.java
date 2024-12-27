package com.erp.erp.global.util.generator;

import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;

public class PlanGenerator extends EntityGenerator{

  public static Plan get() {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .setNull("id")
        .sample();
  }

  public static List<Plan> planList(int size) {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .sampleList(size);
  }

  public static List<Plan> planList(int size, LicenseType licenseType) {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .set("licenseType", licenseType)
        .sampleList(size);
  }
}
