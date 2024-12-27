package com.erp.erp.global.util.generator;


import com.erp.erp.domain.institute.common.entity.Institute;

public class InstituteGenerator extends EntityGenerator{

  public static Institute get() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }
}
