package com.erp.erp.global.util.generator;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;

public class CustomerGenerator extends EntityGenerator {

  public static Customer get(
      Plan plan, Institute institute) {
    PlanPayment planPayment = PlanPaymentGenerator.get(plan);
    List<OtherPayment> otherPaymentList = OtherPaymentGenerator.getList(plan);
    return fixtureMonkey.giveMeBuilder(Customer.class)
        .setNull("id")
        .set("institute", institute)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .sample();
  }

  public static Customer get(
      Plan plan, Institute institute, CustomerStatus customerStatus) {
    PlanPayment planPayment = PlanPaymentGenerator.get(plan);
    List<OtherPayment> otherPaymentList = OtherPaymentGenerator.getList(plan);
    return fixtureMonkey.giveMeBuilder(Customer.class)
        .setNull("id")
        .set("institute", institute)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .set("progress", null)
        .set("status", customerStatus)
        .sample();
  }

  public static Customer get(Plan plan, Institute institute, CustomerStatus status, String name) {
    PlanPayment planPayment = PlanPaymentGenerator.get(plan);
    List<OtherPayment> otherPaymentList = OtherPaymentGenerator.getList(plan);
    return fixtureMonkey.giveMeBuilder(Customer.class)
        .setNull("id")
        .set("institute", institute)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .set("progress", null)
        .set("status", status)
        .set("name", name)
        .sample();
  }
}
