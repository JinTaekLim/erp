package com.erp.erp.domain.customer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.util.ReflectionUtil;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.test.JpaTest;
import com.erp.erp.global.util.generator.ReservationGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
  @Autowired
  private ReservationRepository reservationRepository;

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

  private Customer createCustomers(Institute institute) {
    Plan plan = createPlans();
    Customer customer = CustomerGenerator.get(plan, institute);
    return customerRepository.save(customer);
  }

  private Customer createCustomers(Institute institute, CustomerStatus status) {
    Plan plan = createPlans();
    Customer customer = CustomerGenerator.get(plan, institute, status);
    return customerRepository.save(customer);
  }

  private Reservation createReservations(Customer customer, Institute institute,
      LocalDateTime startTime, LocalDateTime endTime) {
    Reservation reservation = ReservationGenerator.get(customer, institute, startTime, endTime);
    return reservationRepository.save(reservation);
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
    String updatedId = String.valueOf(RandomValue.getInt(0, 10));
    Customer customer = createCustomers();
    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    // when
    customerRepository.updateStatusById(customer.getId() , status, updatedId);
    entityManager.clear();
    Customer testCustomer = customerRepository.findById(customer.getId())
        .orElseThrow(AssertionError::new);

    // then
    assertThat(testCustomer.getStatus()).isEqualTo(status);
    assertThat(testCustomer.getUpdatedId()).isEqualTo(updatedId);
  }

  @Test
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

  @Test
  void findIdsCreatedAtBefore() {
    // given
    int size = RandomValue.getInt(0, 10);

    int day = RandomValue.getInt(1, 100);
    LocalDateTime createdAt = LocalDateTime.now().minusDays(day);

    LocalDateTime startDate = RandomValue.getRandomLocalDateTime().withSecond(0);

    Institute institute = createInstitutes();
    List<Customer> customers = IntStream.range(0, size).mapToObj(i -> {
      Customer customer = createCustomers(institute);
      ReflectionUtil.setFieldValue(customer, "createdAt", createdAt);
      return customer;
    }).toList();

    for(Customer customer : customers) {
      createReservations(customer, institute, startDate, startDate.plusMinutes(30));
      createReservations(customer, institute, LocalDateTime.now(), LocalDateTime.now());
    }

    // when
    List<UpdateCustomerExpiredAtDto> dtoList = customerRepository.findCustomersCreatedAtOnDaysAgo(day);

    // then
    assertThat(dtoList).hasSize(size);

    IntStream.range(0, size).forEach(i -> {
      assertThat(dtoList.get(i).getId()).isEqualTo(customers.get(i).getId());
      assertThat(dtoList.get(i).getFirstReservationDate()).isEqualTo(startDate);
      assertThat(dtoList.get(i).getAvailablePeriod()).isEqualTo(customers.get(i).getPlanPayment().getPlan().getAvailablePeriod());
    });
  }

  @Test
  void updateExpiredAt() {
    // given
    int size = RandomValue.getInt(0, 10);
    Institute institute = createInstitutes();
    LocalDate expiredAt = RandomValue.getRandomLocalDate();

    List<UpdateCustomerExpiredAtDto.Request> req = new ArrayList<>();
    List<Customer> customers = IntStream.range(0, size).mapToObj(i -> {
      Customer customer = createCustomers(institute);

      UpdateCustomerExpiredAtDto.Request update = UpdateCustomerExpiredAtDto.Request.builder()
          .customerId(customer.getId())
          .expiredAt(expiredAt)
          .build();
      req.add(update);

      return customer;
    }).toList();

    // when
    customerRepository.updateExpiredAt(req);
    entityManager.clear();
    List<Customer> customerList = customerRepository.findAll();

    // then
    IntStream.range(0, size).forEach(i -> {
      assertThat(customerList.get(i).getId()).isEqualTo(customers.get(i).getId());
      assertThat(customerList.get(i).getExpiredAt()).isEqualTo(expiredAt);
      assertThat(customerList.get(i).getUpdatedId()).isEqualTo("SERVER");
    });
  }

  @Test
  void findIdsCreatedAtBeforeDaysAgo() {
    // given
    int size = RandomValue.getInt(0, 10);
    Institute institute = createInstitutes();
    LocalDate expiredAt = RandomValue.getRandomLocalDate();

    List<Customer> customers = IntStream.range(0, size).mapToObj(i -> {
      Customer customer = createCustomers(institute, CustomerStatus.ACTIVE);
      ReflectionUtil.setFieldValue(customer, "expiredAt", expiredAt);
      return customer;
    }).toList();

    // when
    List<Long> ids = customerRepository.findIdsCreatedAtBeforeDaysAgo(expiredAt.plusDays(1));

    // then
    assertThat(ids).hasSize(size);
    IntStream.range(0, size).forEach(i -> {
      assertThat(ids.get(i)).isEqualTo(customers.get(i).getId());
    });
  }
}