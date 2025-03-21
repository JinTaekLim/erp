package com.erp.erp.domain.reservation.scheduler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.test.IntegrationTest;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.generator.ReservationGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReservationSchedulerTest extends IntegrationTest {


  int PAGE_SIZE = 20;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private InstituteRepository instituteRepository;

  @Autowired
  private PlanRepository planRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private ReservationCacheManager reservationCacheManager;

  @Autowired
  private ReservationScheduler reservationScheduler;

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }



  private Customer createCustomer(Plan plan, Institute institute, CustomerStatus customerStatus) {
    Customer customer = CustomerGenerator.get(plan, institute, customerStatus);
    return customerRepository.save(customer);
  }

  private Reservation createReservation(Customer customer) {
    return reservationRepository.save(ReservationGenerator.get(customer, customer.getInstitute()));
  }


  @Test
  void updateGetCustomer() {
    //given
    Institute institute = createInstitutes();
    Plan plan = createPlans();
    int customerSize = RandomValue.getInt(1,25);
    CustomerStatus status = CustomerStatus.ACTIVE;

    List<Customer> customers = new ArrayList<>(IntStream.range(0, customerSize)
        .mapToObj(i -> {
          return createCustomer(plan, institute, status);
        }).toList());

    List<Reservation> reservations = customers.stream()
        .flatMap(customer -> {
          int reservationSize = RandomValue.getInt(1, 5);
          return IntStream.range(0, reservationSize)
              .mapToObj(i -> createReservation(customer));
        })
        .toList();

    Long lastId = customers.get(customerSize-1).getId() + 1;

    // when
    reservationScheduler.updateGetCustomer();

    List<GetCustomerDto.Response> cache = reservationCacheManager.getCustomers(institute.getId());
    List<GetCustomerDto.Response> response = customerRepository.findAllByInstituteBeforeIdAndStatus(institute.getId(), lastId, CustomerStatus.ACTIVE, PAGE_SIZE);

    // then
    assertThat(cache.size()).isEqualTo(response.size());
    assertThat(cache).usingRecursiveComparison().isEqualTo(response);
  }
}