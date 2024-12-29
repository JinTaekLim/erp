package com.erp.erp.domain.customer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.test.JpaTest;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomerRepositoryTest extends JpaTest {

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private PlanRepository planRepository;

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }

  private Customer createCustomers() {
    Institute institute = createInstitutes();
    Plan plan = createPlans();
    Customer customer = CustomerGenerator.get(plan, institute);
    return customerRepository.save(customer);
  }


  @Test
  void findAll() {
    // given
    int randomInt = RandomValue.getInt(0, 5);

    IntStream.range(0, randomInt)
        .forEach(i -> createCustomers());

    // when
    List<Customer> customerList = customerRepository.findAll();

    // then
    assertThat(customerList).hasSize(randomInt);
  }

  @Test
  void save() {
    // given
    Customer customer = createCustomers();

    // when
    Long customersId = customer.getId();
    Customer testCustomer = customerRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    // then
    assertThat(testCustomer)
        .usingRecursiveComparison()
        .isEqualTo(customer);
  }

  @Test
  void updateStatusById() {
    // given
    Customer customer = createCustomers();
    long customersId = customer.getId();
    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    // when
    customerRepository.updateStatusById(customersId, status);

    Customer testCustomer = customerRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    CustomerStatus testStatus = testCustomer.getStatus();

    // then
    assertThat(status).isNotEqualTo(testStatus);
  }

  @Test
  @DisplayName("성공")
  void findByInstituteIdAndNameStartingWithAndStatusIn() {
    // given
    Institute institute = createInstitutes();
    Plan plan = createPlans();

    Customer activeCustomer = CustomerGenerator.get(plan, institute, CustomerStatus.ACTIVE);
    customerRepository.save(activeCustomer);

    Customer inactiveCustomer = CustomerGenerator.get(plan, institute, CustomerStatus.INACTIVE);
    customerRepository.save(inactiveCustomer);
    Customer deletedCustomer = CustomerGenerator.get(plan, institute, CustomerStatus.DELETED);
    customerRepository.save(deletedCustomer);

    int randomI = RandomValue.getInt(0, 3);
    Customer customer = (randomI == 0) ? activeCustomer :
                        (randomI == 1) ? inactiveCustomer : deletedCustomer;

    String name = customer.getName();
    String expectedName = String.valueOf(name.charAt(0));

    int randomJ = RandomValue.getInt(0, 3);
    List<CustomerStatus> statuses = new ArrayList<>();
    if (randomJ == 0) {
      statuses.add(CustomerStatus.ACTIVE);
    } else if (randomJ == 1) {
      statuses.add(CustomerStatus.INACTIVE);
    } else if (randomJ == 2) {
      statuses.add(CustomerStatus.DELETED);
    }

    // when
    List<Customer> customers = customerRepository.findByInstituteIdAndNameStartingWithAndStatusIn(
        institute.getId(), expectedName, statuses);

    // then
    if (randomI == randomJ) {
      assertThat(customers.get(0)).usingRecursiveComparison().isEqualTo(customer);
    } else {
      assertThat(customers).isEmpty();
    }
  }
}