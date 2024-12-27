package com.erp.erp.domain.reservation;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.erp.erp.domain.customer.common.dto.AddCustomerDto.Response;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetDailyReservationDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.type.ReservationErrorType;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.OtherPaymentGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.generator.PlanPaymentGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ReservationTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/reservation";
  }

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
  private Customer createCustomers(Institute institute){
    Plan plan = createPlans();
    PlanPayment planPayment = PlanPaymentGenerator.get(plan);
    List<OtherPayment> otherPaymentList = OtherPaymentGenerator.getList(plan);
    Customer customer = CustomerGenerator.get(institute, planPayment, otherPaymentList);
    return customerRepository.save(customer);
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }

  @Test
  void addReservations() {
    // given
    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);
    int randomInt1 = RandomValue.getInt(0, 2);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime = (randomInt1 == 0) ? now.withMinute(0) : now.withMinute(30);

    int randomInt2 = RandomValue.getInt(1,10);
    LocalDateTime endTime = startTime.plusMinutes(30*randomInt2);

    AddReservationDto.Request request = AddReservationDto.Request.builder()
            .customerId(customer.getId())
            .startTime(startTime)
            .endTime(endTime)
            .memo(RandomValue.string(255).get())
        .seatNumber(RandomValue.getInt(1, institute.getTotalSeat()))
            .build();

    // when

    String url = BASE_URL + "/addReservation";


    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url,
            request,
            String.class
    );

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
            responseEntity.getBody(),
            new TypeToken<ApiResult<AddReservationDto.Response>>() {}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getCustomerId()).isEqualTo(request.getCustomerId());
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime().withSecond(0).withNano(0));
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime().withSecond(0).withNano(0));
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
  }

  @Test
  @DisplayName("시작 시간이 종료 시간과 동일하거나 작은 경우")
  void addReservations_fail() {
    // given
    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);
    int randomInt1 = RandomValue.getInt(0, 2);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime = (randomInt1 == 0) ? now.withMinute(0) : now.withMinute(30);

    int randomInt2 = RandomValue.getInt(1,10);
    LocalDateTime endTime = startTime.minusMinutes(30*randomInt2);

    AddReservationDto.Request request = AddReservationDto.Request.builder()
            .customerId(customer.getId())
            .startTime(startTime)
            .endTime(endTime)
            .memo(RandomValue.string(255).get())
        .seatNumber(RandomValue.getInt(1, institute.getTotalSeat()))

        .build();

    InvalidReservationTimeException exception = new InvalidReservationTimeException();

    // when

    String url = BASE_URL + "/addReservation";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url,
            request,
            String.class
    );

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
            responseEntity.getBody(),
            new TypeToken<ApiResult<AddReservationDto.Response>>() {}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getData()).isNull();
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


  @Test
  void getDailyReservations_성공() {
    //given
    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);
    LocalDate day = RandomValue.getRandomLocalDate();

    int reservationsCount = RandomValue.getInt(1,20);
    for( int i =0; i<reservationsCount; i++) {

      int hour = RandomValue.getInt(24);
      int randomInt = RandomValue.getInt(2);
      int minute = (randomInt == 1) ? 0 : 30;
      LocalTime randomTime = LocalTime.of(hour, minute);
      LocalDateTime startTime = LocalDateTime.of(day, randomTime);

      int multiInt = RandomValue.getInt(1,10);
      int plusMinute = 30 * multiInt;
      LocalDateTime endTime = startTime.plusMinutes(plusMinute);


      Reservation reservation = Reservation.builder()
          .institute(institute)
          .customer(customer)
          .startTime(startTime)
          .endTime(endTime)
          .build();

      reservationRepository.save(reservation);
    }


    String url = BASE_URL + "/getDailyReservations?day=" + day;


    //when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetDailyReservationDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetDailyReservationDto.Response>>>(){}.getType()
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(reservationsCount).isEqualTo(apiResponse.getData().size());

  }

  @Test
  void getDailyReservations_예약_없음() {

    //given
    LocalDate day = RandomValue.getRandomLocalDate();

    String url = BASE_URL + "/getDailyReservations?day=" + day;

    ReservationErrorType exception = ReservationErrorType.NOT_FOUND_RESERVATION;


    //when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Response>>(){}
    );

    //then
    assertNotNull(apiResponse);
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());

  }
}