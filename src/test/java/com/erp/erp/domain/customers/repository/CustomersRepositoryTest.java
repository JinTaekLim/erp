package com.erp.erp.domain.customers.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.domain.payments.common.entity.OtherPayments;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.repository.PlansRepository;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.JpaTest;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomersRepositoryTest extends JpaTest {

  @Autowired
  private CustomersRepository customersRepository;
  @Autowired
  private InstitutesRepository institutesRepository;
  @Autowired
  private PlansRepository plansRepository;


  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Customers getCustomers(
          Plans plans, Institutes institutes, PlanPayment planPayment,
          List<OtherPayments> otherPaymentList
  ) {
    return fixtureMonkey.giveMeBuilder(Customers.class)
            .setNull("id")
            .set("plans", plans)
            .set("institutes", institutes)
            .set("planPayment", planPayment)
            .set("otherPayments", otherPaymentList)
            .set("progress", null)
            .sample();
  }

  private Plans getPlans() {
    return fixtureMonkey.giveMeBuilder(Plans.class)
            .setNull("id")
            .sample();
  }

  private Plans createPlans() {
    return plansRepository.save(getPlans());
  }

  private PlanPayment getPlanPayment(Plans plans) {
    return fixtureMonkey.giveMeBuilder(PlanPayment.class)
            .setNull("id")
            .set("plans", plans)
            .sample();
  }

  private List<OtherPayments> getRandomOtherPaymentList(Plans plans) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayments.class)
            .setNull("id")
            .set("plans", plans)
            .sampleList(randomInt);
  }


  @Test
  void findAll() {
    // Given
    int randomInt = faker.random().nextInt(0, 100);

    List<Customers> customersToSave = IntStream.range(0, randomInt)
            .mapToObj(i -> {
              Institutes institutes = createInstitutes();
              Plans plans = createPlans();
              PlanPayment planPayment = getPlanPayment(plans);
              List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plans);

              return getCustomers(plans, institutes, planPayment, otherPaymentList);
            })
            .toList();


    customersRepository.saveAll(customersToSave);

    // When
    List<Customers> customersList = customersRepository.findAll();

    // Then
    assertThat(customersList).hasSize(randomInt);
  }

  @Test
  void save() {
    // Given
    Institutes institutes = createInstitutes();
    Plans plans = createPlans();
    PlanPayment planPayment = getPlanPayment(plans);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plans);
    Customers customers = getCustomers(plans, institutes, planPayment, otherPaymentList);
    customersRepository.save(customers);

    // Then
    Long customersId = customers.getId();
    Customers testCustomers = customersRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    // When
    assertThat(testCustomers)
        .usingRecursiveComparison()
        .isEqualTo(customers);
  }
  @Test
  void updateStatusById() {
    // Given
    Institutes institutes = createInstitutes();
    Plans plans = createPlans();
    PlanPayment planPayment = getPlanPayment(plans);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plans);
    Customers customers = getCustomers(plans, institutes, planPayment, otherPaymentList);
    customersRepository.save(customers);

    long customersId = customers.getId();
    boolean status = !customers.getStatus();

    // Then
    customersRepository.updateStatusById(customersId, status);

    Customers testCustomers = customersRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    boolean testStatus = testCustomers.getStatus();

    // When
    assertThat(status).isNotEqualTo(testStatus);
  }
}