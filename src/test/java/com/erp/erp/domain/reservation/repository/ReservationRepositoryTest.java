package com.erp.erp.domain.reservation.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.generator.ReservationGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.JpaTest;
import java.time.LocalDate;
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

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Customer createCustomers(Institute institute) {
    Plan plan = createPlans();
    Customer customer = CustomerGenerator.get(plan, institute);
    return customerRepository.save(customer);
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }

  private Reservation createReservations(Customer customer, Institute institute,
      LocalDate day, int startIndex, int endIndex) {
    Reservation reservation = ReservationGenerator.get(customer, institute, day, startIndex, endIndex);
    return reservationRepository.save(reservation);
  }

  private Reservation createReservations(Customer customer, Institute institute, LocalDate day) {
    Reservation reservation = ReservationGenerator.get(customer, institute, day);
    return reservationRepository.save(reservation);
  }


//  @Test
//  void findByInstituteAndTimeRange() {
//    // given
//    Institute institute = createInstitutes();
//    Customer customers = createCustomers(institute);
//
//    int returnCount = RandomValue.getInt(0,5);;
//    int reservationCount = RandomValue.getInt(0,5);;
//
//    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withSecond(0);
//    LocalDateTime endTime = startTime.plusMinutes(RandomValue.getInt(30,180));
//    IntStream.range(0, returnCount).forEach(i -> {
//      createReservations(customers, institute, startTime, endTime);
//    });
//
//    IntStream.range(0, reservationCount).forEach(i -> {
//      LocalDateTime start = startTime.plusMinutes(RandomValue.getInt(180,1800));
//      LocalDateTime end = start.plusMinutes(RandomValue.getInt(30,1800));
//      createReservations(customers, institute, start, end);
//    });
//
//    // when
//    List<Reservation> reservations = reservationRepository.findByInstituteAndTimeRange(institute, startTime, endTime);
//
//    // then
//    assertThat(reservations.size()).isEqualTo(returnCount);
//  }

  @Test
  void findByInstituteAndStartDate() {
    // given
    Institute institute = createInstitutes();

    int reservationCount = RandomValue.getInt(0,5);
    int nonReturnCount = RandomValue.getInt(0,5);

    LocalDate day = RandomValue.getRandomLocalDate();

    IntStream.range(0, reservationCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, day);
    });


    LocalDate nonReturnDay = day.plusDays(RandomValue.getInt(1,10));

    IntStream.range(0, nonReturnCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, nonReturnDay);
    });

    // when
    List<Reservation> reservations = reservationRepository.findByInstituteAndStartDate(institute, day);

    // then
    assertThat(reservations.size()).isEqualTo(reservationCount);
  }

  @Test
  void findByInstituteWithOverlappingTimeRange() {
    // given
    Institute institute = createInstitutes();

    int reservationCount = RandomValue.getInt(0,5);
    int nonReturnCount = RandomValue.getInt(0,5);

    LocalDate day = RandomValue.getRandomLocalDate();
    int startIndex = RandomValue.getInt(10,20);
    int endIndex = RandomValue.getInt(30,40);

    IntStream.range(0, reservationCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, day, startIndex, endIndex);
    });

    int nonReturnStartIndex = RandomValue.getInt(21,29);
    int nonReturnEndIndex = RandomValue.getInt(41,49);

    IntStream.range(0, nonReturnCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, day, nonReturnStartIndex, nonReturnEndIndex);
    });

    // when
    List<Reservation> reservations = reservationRepository.findReservationsWithinTimeRange(
        institute, day, startIndex, endIndex
    );

    // then
    assertThat(reservations.size()).isEqualTo(reservationCount);
  }
}