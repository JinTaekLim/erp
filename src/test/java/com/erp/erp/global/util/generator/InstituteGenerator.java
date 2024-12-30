package com.erp.erp.global.util.generator;


import com.erp.erp.domain.institute.common.entity.Institute;

public class InstituteGenerator extends EntityGenerator{

  public static Institute get() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }

  public static Institute get(int totalSeat) {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .set("totalSeat", totalSeat)
        .sample();
  }
}
