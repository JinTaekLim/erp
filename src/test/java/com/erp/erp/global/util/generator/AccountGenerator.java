package com.erp.erp.global.util.generator;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.institute.common.entity.Institute;

public class AccountGenerator extends EntityGenerator{

  public static Account get(Institute institute) {
    return fixtureMonkey.giveMeBuilder(Account.class)
        .setNull("id")
        .set("institute", institute)
        .sample();
  }
}
