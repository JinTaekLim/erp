package com.erp.erp.domain.customer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.payments.common.entity.OtherPayments;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.JpaTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomerRepositoryTest extends JpaTest {

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private PlanRepository planRepository;


  private Institute getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }

  private Institute createInstitutes() {
    return instituteRepository.save(getInstitutes());
  }

  private Customer getCustomers(Institute institute, PlanPayment planPayment, List<OtherPayments> otherPaymentList) {
    return fixtureMonkey.giveMeBuilder(Customer.class)
            .setNull("id")
            .set("institute", institute)
            .set("planPayment", planPayment)
            .set("otherPayments", otherPaymentList)
            .set("progress", null)
            .sample();
  }

  private Plan getPlans() {
    return fixtureMonkey.giveMeBuilder(Plan.class)
            .setNull("id")
            .sample();
  }

  private Plan createPlans() {
    return planRepository.save(getPlans());
  }

  private PlanPayment getPlanPayment(Plan plan) {
    return fixtureMonkey.giveMeBuilder(PlanPayment.class)
            .setNull("id")
            .set("plan", plan)
            .sample();
  }

  private List<OtherPayments> getRandomOtherPaymentList(Plan plan) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayments.class)
            .setNull("id")
            .set("plans", plan)
            .sampleList(randomInt);
  }


  @Test
  void findAll() {
    // Given
    int randomInt = faker.random().nextInt(0, 100);

    List<Customer> customerToSave = IntStream.range(0, randomInt)
            .mapToObj(i -> {
              Institute institute = createInstitutes();
              Plan plan = createPlans();
              PlanPayment planPayment = getPlanPayment(plan);
              List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plan);

              return getCustomers(institute, planPayment, otherPaymentList);
            })
            .toList();


    customerRepository.saveAll(customerToSave);

    // When
    List<Customer> customerList = customerRepository.findAll();

    // Then
    assertThat(customerList).hasSize(randomInt);
  }

  @Test
  void save() {
    // Given
    Institute institute = createInstitutes();
    Plan plan = createPlans();
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plan);
    Customer customer = getCustomers(institute, planPayment, otherPaymentList);
    customerRepository.save(customer);

    // Then
    Long customersId = customer.getId();
    Customer testCustomer = customerRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    // When
    assertThat(testCustomer)
        .usingRecursiveComparison()
        .isEqualTo(customer);
  }
  @Test
  void updateStatusById() {
    // Given
    Institute institute = createInstitutes();
    Plan plan = createPlans();
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plan);
    Customer customer = getCustomers(institute, planPayment, otherPaymentList);
    customerRepository.save(customer);
    long customersId = customer.getId();
    CustomerStatus status = Arrays.stream(CustomerStatus.values())
        .filter(s -> s != customer.getStatus())
        .findAny()
        .orElseThrow(IllegalStateException::new);

    // Then
    customerRepository.updateStatusById(customersId, status);

    Customer testCustomer = customerRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    CustomerStatus testStatus = testCustomer.getStatus();

    // When
    assertThat(status).isNotEqualTo(testStatus);
  }
}