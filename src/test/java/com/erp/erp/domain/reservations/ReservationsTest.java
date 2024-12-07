package com.erp.erp.domain.reservations;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.erp.erp.domain.customers.common.dto.AddCustomerDto.Response;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.domain.payments.common.entity.OtherPayments;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import com.erp.erp.domain.plans.common.entity.Plans;
import com.erp.erp.domain.plans.repository.PlansRepository;
import com.erp.erp.domain.reservations.common.dto.GetDailyReservationsDto;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.common.exception.ReservationsErrorType;
import com.erp.erp.domain.reservations.repository.ReservationsRepository;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ReservationsTest extends IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private InstitutesRepository institutesRepository;
  @Autowired
  private CustomersRepository customersRepository;
  @Autowired
  private ReservationsRepository reservationsRepository;
  @Autowired
  private PlansRepository plansRepository;


  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Customers getCustomers(Institutes institutes) {
    Plans plans = createPlans();
    PlanPayment planPayment = getPlanPayment(plans);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plans);

    return fixtureMonkey.giveMeBuilder(Customers.class)
            .setNull("id")
            .set("plans", plans)
            .set("institutes", institutes)
            .set("planPayment", planPayment)
            .set("otherPayments", otherPaymentList)
            .set("progress", null)
            .sample();
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }
  private Customers createCustomers(Institutes institutes){
    Customers customers = getCustomers(institutes);
    return customersRepository.save(customers);
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

  private Plans getPlans() {
    return fixtureMonkey.giveMeBuilder(Plans.class)
            .setNull("id")
            .sample();
  }

  private Plans createPlans() {
    return plansRepository.save(getPlans());
  }

//  @Test
//  void addReservations() {
//    // given
//    Customers customers = createCustomers();
//    LocalDateTime startTime = LocalDateTime.now();
//
//    int randomInt = RandomValue.getInt(0,600);
//    LocalDateTime endTime = LocalDateTime.now().plusMinutes(30+randomInt);
//
//    AddReservationsDto.Request request = AddReservationsDto.Request.builder()
//            .customersId(customers.getId())
//            .startTime(startTime)
//            .endTime(endTime)
//            .memo(RandomValue.string(255).get())
//            .build();
//
//    // when
//
//    String url = "http://localhost:" + port + "/api/reservation/addReservations";
//
//
//    //when
//    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
//            url,
//            request,
//            String.class
//    );
//
//    ApiResult<AddReservationsDto.Response> apiResponse = gson.fromJson(
//            responseEntity.getBody(),
//            new TypeToken<ApiResult<AddReservationsDto.Response>>() {}.getType()
//    );
//
//    // then
//    AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertNotNull(apiResponse.getData());
//    assertThat(apiResponse.getData().getCustomersId()).isEqualTo(request.getCustomersId());
//    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime().withNano(0));
//    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime().withNano(0));
//    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
//  }
//
//  @Test
//  @DisplayName("시작 시간이 종료 시간과 동일하거나 작은 경우")
//  void addReservations_fail() {
//    // given
//    Customers customers = createCustomers();
//    LocalDateTime startTime = LocalDateTime.now();
//    int randomInt = RandomValue.getInt(0,600);
//    LocalDateTime endTime = startTime.minusMinutes(randomInt);
//
//    AddReservationsDto.Request request = AddReservationsDto.Request.builder()
//            .customersId(customers.getId())
//            .startTime(startTime)
//            .endTime(endTime)
//            .memo(RandomValue.string(255).get())
//            .build();
//
//    InvalidReservationTimeException exception = new InvalidReservationTimeException();
//
//    // when
//
//    String url = "http://localhost:" + port + "/api/reservation/addReservations";
//
//
//    //when
//    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
//            url,
//            request,
//            String.class
//    );
//
//    ApiResult<AddReservationsDto.Response> apiResponse = gson.fromJson(
//            responseEntity.getBody(),
//            new TypeToken<ApiResult<AddReservationsDto.Response>>() {}.getType()
//    );
//
//    // then
//    AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    assertNull(apiResponse.getData());
//    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
//    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
//  }


  @Test
  void getDailyReservations_성공() {
    //given
    Institutes institutes = createInstitutes();
    Customers customers = createCustomers(institutes);
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


      Reservations reservations = Reservations.builder()
          .institutes(institutes)
          .customers(customers)
          .startTime(startTime)
          .endTime(endTime)
          .build();

      reservationsRepository.save(reservations);
    }


    String url = "http://localhost:" + port + "/api/reservation/getDailyReservations"
        +"?day=" + day;


    //when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetDailyReservationsDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetDailyReservationsDto.Response>>>(){}.getType()
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

    String url = "http://localhost:" + port + "/api/reservation/getDailyReservations"
        +"?day=" + day;

    ReservationsErrorType exception = ReservationsErrorType.NOT_FOUND_RESERVATIONS;


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