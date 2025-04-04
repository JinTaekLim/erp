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
      LocalDateTime startTime, LocalDateTime endTime) {
    Reservation reservation = ReservationGenerator.get(customer, institute, startTime, endTime);
    return reservationRepository.save(reservation);
  }


  @Test
  void findByInstituteAndTimeRange() {
    // given
    Institute institute = createInstitutes();
    Customer customers = createCustomers(institute);

    int returnCount = RandomValue.getInt(0,5);;
    int reservationCount = RandomValue.getInt(0,5);;

    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withSecond(0);
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
    List<Reservation> reservations = reservationRepository.findByInstituteAndTimeRange(institute, startTime, endTime);

    // then
    assertThat(reservations.size()).isEqualTo(returnCount);
  }

  @Test
  void findByInstituteAndStartDate() {
    // given
    Institute institute = createInstitutes();

    int reservationCount = RandomValue.getInt(0,5);
    int nonReturnCount = RandomValue.getInt(0,5);

    LocalDateTime startTime = RandomValue.getRandomLocalDateTime();
    LocalDateTime endTime = startTime.plusMinutes(RandomValue.getInt(30,180));

    IntStream.range(0, reservationCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, startTime, endTime);
    });

    LocalDateTime start = endTime.plusDays(RandomValue.getInt(1,10));
    LocalDateTime end = start.plusDays(RandomValue.getInt(1,10));

    IntStream.range(0, nonReturnCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, start, end);
    });

    // when
    List<Reservation> reservations = reservationRepository.findByInstituteAndStartDate(institute, startTime.toLocalDate());

    // then
    assertThat(reservations.size()).isEqualTo(reservationCount);
  }

  @Test
  void findByInstituteWithOverlappingTimeRange() {
    // given
    Institute institute = createInstitutes();

    int reservationCount = RandomValue.getInt(0,5);
    int nonReturnCount = RandomValue.getInt(0,5);

    LocalDateTime startTime = RandomValue.getRandomLocalDateTime();
    LocalDateTime endTime = startTime.plusMinutes(RandomValue.getInt(30,180));

    IntStream.range(0, reservationCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, startTime, endTime);
    });

    LocalDateTime start = endTime.plusDays(RandomValue.getInt(1,10));
    LocalDateTime end = start.plusDays(RandomValue.getInt(1,10));

    IntStream.range(0, nonReturnCount).forEach(i -> {
      Customer customers = createCustomers(institute);
      createReservations(customers, institute, start, end);
    });

    // when
    List<Reservation> reservations = reservationRepository.findByInstituteWithOverlappingTimeRange(institute, startTime, endTime);

    // then
    assertThat(reservations.size()).isEqualTo(reservationCount);
  }
}