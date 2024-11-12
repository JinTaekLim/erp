package com.erp.erp.domain.reservations;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.erp.erp.domain.customers.common.dto.AddCustomerDto.Response;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
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
import org.assertj.core.api.AssertionsForClassTypes;
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


  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Customers getCustomers() {
    Institutes institutes = createInstitutes();

    return fixtureMonkey.giveMeBuilder(Customers.class)
        .setNull("id")
        .set("institutes", institutes)
        .sample();
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }
  private Customers createCustomers(){ return customersRepository.save(getCustomers());}

  @Test
  void getDailyReservations_성공() {
    //given

    Customers customers = createCustomers();
    Institutes institutes = customers.getInstitutes();
    LocalDate day = RandomValue.getRandomLocalDate();

    int reservationsCount = RandomValue.getInt(1,20);
    for( int i =0; i<reservationsCount; i++) {

      int hour = RandomValue.getInt(24);
      int randomInt = RandomValue.getInt(2);
      int minute = (randomInt == 1) ? 0 : 30;

      LocalTime randomTime = LocalTime.of(hour, minute);
      LocalDateTime startTime = LocalDateTime.of(day, randomTime);
      LocalDateTime endTime = startTime.plusMinutes(minute);


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
    AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    int reservationsSize = apiResponse.getData().size();
    assertThat(reservationsCount).isEqualTo(reservationsSize);

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