package com.erp.erp.domain.reservation;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.common.exception.NotFoundProgressException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.customer.repository.ProgressRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.domain.reservation.business.ReservationSender;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetDailyReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetReservationCustomerDetailsDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedSeatNumberDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.InvalidSeatRangeException;
import com.erp.erp.domain.reservation.common.exception.NotFoundReservationException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.HttpEntityUtil;
import com.erp.erp.global.util.generator.AccountGenerator;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.generator.ProgressGenerator;
import com.erp.erp.global.util.generator.ReservationGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private ProgressRepository progressRepository;
  @Autowired
  private TokenManager tokenManager;
  @MockBean
  private ReservationSender reservationSender;

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Institute createInstitutes(int totalSeat) {
    return instituteRepository.save(InstituteGenerator.get(totalSeat));
  }

  private Customer createCustomers(Institute institute) {
    Plan plan = createPlans();
    Customer customer = CustomerGenerator.get(plan, institute);
    return customerRepository.save(customer);
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }

  private Account createAccount() {
    Institute institute = createInstitutes();
    return accountRepository.save(AccountGenerator.get(institute));
  }

  private Account createAccount(Institute institute) {
    return accountRepository.save(AccountGenerator.get(institute));
  }

  private Reservation createReservation(Customer customer, Institute institute,
      LocalDate day) {
    Reservation reservation = ReservationGenerator.get(customer, institute, day);
    return reservationRepository.save(reservation);
  }

  private Reservation createReservation(Customer customer, Institute institute) {
    Reservation reservation = ReservationGenerator.get(customer, institute);
    return reservationRepository.save(reservation);
  }

  private List<Progress> createProgressList(Customer customer, int size) {
    List<Progress> progressList = ProgressGenerator.get(customer, size);
    progressRepository.saveAll(progressList);
    return progressList;
  }

  @Test
  @DisplayName("addReservations 성공")
  void addReservations() {
    // given
    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Reservation reservation = ReservationGenerator.get(customer, institute);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    AddReservationDto.Request req = AddReservationDto.Request.builder()
        .customerId(customer.getId())
        .reservationDate(reservation.getReservationDate())
        .startIndex(reservation.getStartIndex())
        .endIndex(reservation.getEndIndex())
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/addReservation";

    HttpEntity<AddReservationDto.Request> httpRequest = HttpEntityUtil.setToken(
        req,
        tokenDto.getAccessToken()
    );

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        httpRequest,
        String.class
    );

    ApiResult<Void> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Void>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNull(apiResponse.getData());
  }

  @Test
  @DisplayName("addReservations 다른 매장의 고객 예약")
  void addReservations_fail() {
    // given
    Account account = createAccount();
    TokenDto tokenDto = tokenManager.createToken(account);

    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);

    AddReservationDto.Request request = fixtureMonkey.giveMeBuilder(AddReservationDto.Request.class)
        .set("customerId", customer.getId())
        .sample();

    NotFoundCustomerException exception = new NotFoundCustomerException();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<AddReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    String url = BASE_URL + "/addReservation";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        requestEntity,
        String.class
    );

    ApiResult<Void> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Void>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getData()).isNull();
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


  @Test
  @DisplayName("addReservations 시작 시간이 종료 시간과 동일하거나 작은 경우")
  void addReservations_fail_2() {
    // given
    Institute institute = createInstitutes();
    Customer customer = createCustomers(institute);
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Reservation reservation = ReservationGenerator.get(customer, institute);
    int endIndex = RandomValue.getInt(0, reservation.getStartIndex());

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    AddReservationDto.Request req = AddReservationDto.Request.builder()
        .customerId(customer.getId())
        .reservationDate(reservation.getReservationDate())
        .startIndex(reservation.getStartIndex())
        .endIndex(endIndex)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .build();

    InvalidReservationTimeException exception = new InvalidReservationTimeException();

    String url = BASE_URL + "/addReservation";

    HttpEntity<AddReservationDto.Request> httpRequest = HttpEntityUtil.setToken(
        req,
        tokenDto.getAccessToken()
    );

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        httpRequest,
        String.class
    );

    ApiResult<Void> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Void>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getData()).isNull();
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("getDailyReservations 성공")
  void getDailyReservations() {
    //given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    LocalDate day = RandomValue.getRandomLocalDate();

    int reservationsCount = RandomValue.getInt(0, 5);
    List<Reservation> reservations = IntStream.range(0, reservationsCount).mapToObj(i -> {
      return createReservation(customer, institute, day);
    }).toList();

    int nonReturnReservationCount = RandomValue.getInt(0, 5);
    Institute nonReturnInstitute = createInstitutes();
    Customer nonReturnCustomer = createCustomers(nonReturnInstitute);
    IntStream.range(0, nonReturnReservationCount).forEach(i -> {
      createReservation(nonReturnCustomer, nonReturnInstitute);
    });


    String url = BASE_URL + "/getDailyReservations?day=" + day;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdateCustomerDto.Request> requestEntity = new HttpEntity<>(headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<List<GetDailyReservationDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetDailyReservationDto.Response>>>() {
        }.getType()
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(reservationsCount).isEqualTo(apiResponse.getData().size());
    IntStream.range(0, reservationsCount).forEach(i -> {
      assertThat(apiResponse.getData().get(i).getReservationDate()).isEqualTo(reservations.get(i).getReservationDate());
      assertThat(apiResponse.getData().get(i).getStartIndex()).isEqualTo(reservations.get(i).getStartIndex());
      assertThat(apiResponse.getData().get(i).getEndIndex()).isEqualTo(reservations.get(i).getEndIndex());
      assertThat(apiResponse.getData().get(i).getSeatNumber()).isEqualTo(reservations.get(i).getSeatNumber());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(reservations.get(i).getCustomer().getName());
    });
  }

  @Test
  @DisplayName("updatedReservation 진도표 미변경 성공")
  void updatedReservation() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Progress> progressList = createProgressList(customer, progressSize);

    Reservation reservation = createReservation(customer, institute);

    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .build();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("updatedReservation 진도표 추가 성공")
  void updatedReservation_success_1() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Progress> progressList = createProgressList(customer, progressSize);

    int addProgressSize = RandomValue.getInt(1,5);;

    List<ProgressDto.Request> progressRequest = fixtureMonkey.giveMeBuilder(ProgressDto.Request.class)
        .setNull("progressId")
        .set("deleted", false)
        .sampleList(addProgressSize);

    List<ProgressDto.Request> allProgress = Stream.concat(
        progressList.stream().map(progress -> ProgressDto.Request.builder()
            .progressId(progress.getId())
            .date(progress.getDate())
            .content(progress.getContent())
            .deleted(false)
            .build()),
        progressRequest.stream()
    ).collect(Collectors.toCollection(ArrayList::new));

    Reservation reservation = createReservation(customer, institute);
    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .progressList(progressRequest)
        .build();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("updatedReservation 진도표 수정 성공")
  void updatedReservation_success_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Progress> progressList = createProgressList(customer, progressSize);

    List<ProgressDto.Request> progressRequest = progressList.stream()
        .map(progress -> {
          return fixtureMonkey.giveMeBuilder(ProgressDto.Request.class)
              .set("progressId", progress.getId())
              .set("deleted", false)
              .sample();
        }).toList();

    Reservation reservation = createReservation(customer, institute);
    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .progressList(progressRequest)
        .build();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("updatedReservation 진도표 삭제 성공")
  void updatedReservation_success_3() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(1, 5);;
    List<Progress> progressList = createProgressList(customer, progressSize);

    int deleteProgressSize = RandomValue.getInt(1, progressSize);
    List<ProgressDto.Request> progressRequest = IntStream.range(0, deleteProgressSize)
        .mapToObj(i -> {
          return fixtureMonkey.giveMeBuilder(ProgressDto.Request.class)
              .set("progressId", progressList.get(i).getId())
              .set("deleted", true)
              .sample();
        }).toList();

    Reservation reservation = createReservation(customer, institute);
    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .progressList(progressRequest)
        .build();


    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("updatedReservation 존재하지 않는 예약")
  void updatedReservation_fail() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    UpdatedReservationDto.Request request = fixtureMonkey.giveMeBuilder(UpdatedReservationDto.Request.class)
        .set("reservationId", RandomValue.getRandomLong(9999))
        .sample();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedReservation 다른 매장 예약")
  void updatedReservation_fail_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Institute nonReturnInstitute = createInstitutes();
    Customer customer = createCustomers(nonReturnInstitute);
    Reservation reservation = createReservation(customer, nonReturnInstitute);

    UpdatedReservationDto.Request request = fixtureMonkey.giveMeBuilder(UpdatedReservationDto.Request.class)
        .set("reservationId", reservation.getId())
        .sample();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedReservation 잘못된 seatNumber")
  void updatedReservation_fail_3() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);
    Customer customer = createCustomers(institute);
    Reservation reservation = createReservation(customer, institute);

    Reservation newReservation = ReservationGenerator.get(customer, institute);
    int seatNumber = RandomValue.getInt(0,2) == 0 ? 0 : institute.getTotalSeat() + RandomValue.getInt(1,5);
    UpdatedReservationDto.Request request = fixtureMonkey.giveMeBuilder(UpdatedReservationDto.Request.class)
        .set("startIndex", newReservation.getStartIndex())
        .set("endIndex", newReservation.getEndIndex())
        .set("reservationId", reservation.getId())
        .set("seatNumber", seatNumber)
        .sample();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    InvalidSeatRangeException exception = new InvalidSeatRangeException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedReservation 진도표 수정 잘못된 ID값 입력")
  void updatedReservation_fail_4() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Long> progressIds = createProgressList(customer, progressSize).stream()
        .map(Progress::getId)
        .toList();

    long newId = Stream.generate(() -> RandomValue.getRandomLong(0, 99999))
        .filter(id -> !progressIds.contains(id))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("새로운 ID를 생성할 수 없습니다."));

    List<ProgressDto.Request> progressRequest = fixtureMonkey.giveMeBuilder(ProgressDto.Request.class)
        .set("progressId", newId)
        .set("deleted", false)
        .sampleList(1);

    Reservation reservation = createReservation(customer, institute);
    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .progressList(progressRequest)
        .build();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundProgressException exception = new NotFoundProgressException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedReservation 진도표 삭제 잘못된 ID값 입력")
  void updatedReservation_fail_5() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Long> progressIds = createProgressList(customer, progressSize).stream()
        .map(Progress::getId)
        .toList();

    long newId = Stream.generate(() -> RandomValue.getRandomLong(0, 99999))
        .filter(id -> !progressIds.contains(id))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("새로운 ID를 생성할 수 없습니다."));

    List<ProgressDto.Request> progressRequest = fixtureMonkey.giveMeBuilder(ProgressDto.Request.class)
        .set("progressId", newId)
        .set("deleted", true)
        .sampleList(1);

    Reservation reservation = createReservation(customer, institute);
    Reservation newReservation = ReservationGenerator.get(customer, institute);

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .reservationDate(newReservation.getReservationDate())
        .startIndex(newReservation.getStartIndex())
        .endIndex(newReservation.getEndIndex())
        .memo(newReservation.getMemo())
        .seatNumber(newReservation.getSeatNumber())
        .attendanceStatus(newReservation.getAttendanceStatus())
        .progressList(progressRequest)
        .build();

    String url = BASE_URL + "/updatedReservation";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedReservationDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundProgressException exception = new NotFoundProgressException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedSeatNumber 성공")
  void updatedSeatNumber() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    Reservation reservation = createReservation(customer, institute);
    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());

    UpdatedSeatNumberDto.Request request = UpdatedSeatNumberDto.Request.builder()
        .reservationId(reservation.getId())
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/updatedSeatNumber";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedSeatNumberDto.Request> requestEntity = new HttpEntity<>(request, headers);

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedSeatNumberDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedSeatNumberDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData().getReservationId()).isEqualTo(reservation.getId());
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(seatNumber);
  }

  @Test
  @DisplayName("updatedSeatNumber 다른 매장 예약")
  void updatedSeatNumber_fail() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Institute nonReturnInstitute = createInstitutes();
    Customer customer = createCustomers(nonReturnInstitute);
    Reservation reservation = createReservation(customer, nonReturnInstitute);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    UpdatedSeatNumberDto.Request request = UpdatedSeatNumberDto.Request.builder()
        .reservationId(reservation.getId())
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/updatedSeatNumber";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedSeatNumberDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedSeatNumberDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedSeatNumberDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedSeatNumber 존재하지 않는 예약")
  void updatedSeatNumber_fail_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    UpdatedSeatNumberDto.Request request = UpdatedSeatNumberDto.Request.builder()
        .reservationId(RandomValue.getRandomLong(9999))
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/updatedSeatNumber";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedSeatNumberDto.Request> requestEntity = new HttpEntity<>(request, headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedSeatNumberDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedSeatNumberDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updatedSeatNumber 잘못된 seatNumber")
  void updatedSeatNumber_fail_3() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    Reservation reservation = createReservation(customer, institute);

    int seatNumber = RandomValue.getInt(0,2) == 0 ? 0 : institute.getTotalSeat() + RandomValue.getInt(1,5);
    UpdatedSeatNumberDto.Request request = UpdatedSeatNumberDto.Request.builder()
        .reservationId(reservation.getId())
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/updatedSeatNumber";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdatedSeatNumberDto.Request> requestEntity = new HttpEntity<>(request, headers);

    InvalidSeatRangeException exception = new InvalidSeatRangeException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<UpdatedSeatNumberDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdatedSeatNumberDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("deleteReservation 성공")
  void deleteReservation() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    Reservation reservation = createReservation(customer, institute);

    String url = BASE_URL + "/deleteReservation/" + reservation.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        requestEntity,
        String.class
    );

    ApiResult<Boolean> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Boolean>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData()).isEqualTo(true);
    assertThrows(AssertionError.class,
        () -> reservationRepository.findByIdAndInstituteId(reservation.getId(), institute.getId())
            .orElseThrow(AssertionError::new));
  }

  @Test
  @DisplayName("deleteReservation 존재하지 않는 예약")
  void deleteReservation_fail() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/deleteReservation/" + RandomValue.getRandomLong(999);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        requestEntity,
        String.class
    );

    ApiResult<Boolean> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Boolean>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("deleteReservation 다른 매장 예약")
  void deleteReservation_fail_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Institute nonReturnInstitute = createInstitutes();
    Customer customer = createCustomers(nonReturnInstitute);
    Reservation reservation = createReservation(customer, nonReturnInstitute);

    String url = BASE_URL + "/deleteReservation/" + reservation.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        requestEntity,
        String.class
    );

    ApiResult<Boolean> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Boolean>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("getReservationCustomerDetails 성공")
  void getReservationCustomerDetails() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);

    int progressSize = RandomValue.getInt(0,5);
    List<Progress> progressList = createProgressList(customer, progressSize);

    Reservation reservation = createReservation(customer, institute);

    String url = BASE_URL + "/getReservationCustomerDetails/" + reservation.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetReservationCustomerDetailsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetReservationCustomerDetailsDto.Response>>() {
        }.getType()
    );

    // then
    Plan plan = customer.getPlanPayment().getPlan();
    LocalDateTime registrationAt = customer.getPlanPayment().getRegistrationAt();
    int availablePeriod = plan.getAvailablePeriod();
    LocalDateTime endDate = registrationAt.plusDays(availablePeriod);


    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData().getReservationDate()).isEqualTo(reservation.getReservationDate());
    assertThat(apiResponse.getData().getStartIndex()).isEqualTo(reservation.getStartIndex());
    assertThat(apiResponse.getData().getEndIndex()).isEqualTo(reservation.getEndIndex());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(customer.getName());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(customer.getPhone());
    assertThat(apiResponse.getData().getPlanName()).isEqualTo(plan.getName());
    assertThat(apiResponse.getData().getPlanEndDate()).isEqualTo(endDate);
//    assertThat(apiResponse.getData().getRemainingTime())
//    assertThat(apiResponse.getData().getUsedTime())
    assertThat(apiResponse.getData().getMemo()).isEqualTo(customer.getMemo());

    List<ProgressDto.Response> actualProgress = apiResponse.getData().getProgressList();
    assertThat(actualProgress.size()).isEqualTo(progressSize);
    IntStream.range(0, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getProgressId()).isEqualTo(progressList.get(i).getId());
          assertThat(actualProgress.get(i).getContent()).isEqualTo(progressList.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(progressList.get(i).getDate());
        });

  }

  @Test
  @DisplayName("getReservationCustomerDetails 존재하지 않는 예약")
  void getReservationCustomerDetails_fail() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/getReservationCustomerDetails/" + RandomValue.getRandomLong(999);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetReservationCustomerDetailsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetReservationCustomerDetailsDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("getReservationCustomerDetails 다른 매장 회원")
  void getReservationCustomerDetails_fail_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Institute nonReturnInstitute = createInstitutes();
    Customer customer = createCustomers(nonReturnInstitute);

    Reservation reservation = createReservation(customer, nonReturnInstitute);

    String url = BASE_URL + "/getReservationCustomerDetails/" + reservation.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    NotFoundReservationException exception = new NotFoundReservationException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetReservationCustomerDetailsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetReservationCustomerDetailsDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

}