package com.erp.erp.domain.reservation.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.JpaTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReservationRepositoryTest extends JpaTest {

  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private PlanRepository planRepository;


  private Institute getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }

  private Customer getCustomers(Institute institute) {
    Plan plan = createPlans();
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayment> otherPaymentList = getRandomOtherPaymentList(plan);

    return fixtureMonkey.giveMeBuilder(Customer.class)
        .setNull("id")
        .set("institute", institute)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .set("progress", null)
        .sample();
  }

  private Institute createInstitutes() {
    return instituteRepository.save(getInstitutes());
  }

  private Customer createCustomers(Institute institute) {
    Customer customer = getCustomers(institute);
    return customerRepository.save(customer);
  }

  private PlanPayment getPlanPayment(Plan plan) {
    return fixtureMonkey.giveMeBuilder(PlanPayment.class)
        .setNull("id")
        .set("plan", plan)
        .sample();
  }

  private List<OtherPayment> getRandomOtherPaymentList(Plan plan) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayment.class)
        .setNull("id")
        .set("plan", plan)
        .sampleList(randomInt);
  }

  private Plan getPlans() {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .setNull("id")
        .sample();
  }

  private Plan createPlans() {
    return planRepository.save(getPlans());
  }

  private Reservation getReservations(Customer customer, Institute institute,
      LocalDateTime startTime, LocalDateTime endTime) {
    return Reservation.builder()
        .customer(customer)
        .institute(institute)
        .startTime(startTime)
        .endTime(endTime)
        .build();
  }

  private Reservation createReservations(Customer customer, Institute institute,
      LocalDateTime startTime, LocalDateTime endTime) {
    Reservation reservation = getReservations(customer, institute, startTime, endTime);
    return reservationRepository.save(reservation);
  }


//  @Test
//  void findByInstituteAndTimeRange() {
//    // given
//    Institute institute = createInstitutes();
//    Customer customers = createCustomers(institute);
//
//    LocalDateTime startTime = LocalDateTime.of(2024,12,25,9,30);
//    LocalDateTime endTime = LocalDateTime.of(2024,12,25,10,30);
//    createReservations(customers, institute, startTime, endTime);
//
//    LocalDateTime startTime1 = LocalDateTime.of(2024,12,25,10,30,22,22);
//    LocalDateTime endTime1 = LocalDateTime.of(2024,12,25,11,30,22,22);
//    createReservations(customers, institute, startTime1, endTime1);
//
//    LocalDateTime startTime2 = LocalDateTime.of(2024,12,25,10,31,22,22);
//    LocalDateTime endTime2 = LocalDateTime.of(2024,12,25,11,30,22,22);
//    createReservations(customers, institute, startTime2, endTime2);
//
//    // when
//    List<Reservation> reservations = reservationRepository.findByInstituteAndTimeRange(institute, startTime, endTime);
//
//    // then
//    for (Reservation reservation : reservations) {
//      System.out.println("예약 " + reservation.getId());
//    }
//    assertThat(reservations.size()).isEqualTo(1);
//  }


  @Test
  void findByInstituteAndTimeRange() {
    // given
    Institute institute = createInstitutes();
    Customer customers = createCustomers(institute);

    int returnCount = 3;
    int reservationCount = 3;

    LocalDateTime startTime = RandomValue.getRandomLocalDateTime();
    LocalDateTime endTime = startTime.plusMinutes(RandomValue.getInt(30,180));
    IntStream.range(0, returnCount).forEach(i -> {
      createReservations(customers, institute, startTime, endTime);
    });

    IntStream.range(0, reservationCount).forEach(i -> {
      LocalDateTime start = startTime.plusMinutes(RandomValue.getInt(180,1800));
      LocalDateTime end = start.plusMinutes(RandomValue.getInt(30,1800));
      createReservations(customers, institute, start, end);
    });

    // when
    List<Reservation> reservations = reservationRepository.findByInstituteAndTimeRange(institute, startTime.withSecond(0).withNano(0), endTime.withSecond(0).withNano(0));

    // then
    assertThat(reservations.size()).isEqualTo(returnCount);
  }
}