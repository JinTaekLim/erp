package com.erp.erp.global.util.generator;

import com.erp.erp.domain.admin.common.entity.Admin;

public class AdminGenerator extends EntityGenerator{

  public static Admin get() {
    return fixtureMonkey.giveMeBuilder(Admin.class)
        .setNull("id")
        .sample();
  }
}
