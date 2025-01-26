package com.erp.erp.domain.reservation;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto.AddProgress;
import com.erp.erp.domain.customer.common.dto.ProgressDto.DeleteProgress;
import com.erp.erp.domain.customer.common.dto.ProgressDto.ProgressResponse;
import com.erp.erp.domain.customer.common.dto.ProgressDto.UpdateProgress;
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
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetDailyReservationDto;
import com.erp.erp.domain.reservation.common.dto.GetReservationCustomerDetailsDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedReservationDto;
import com.erp.erp.domain.reservation.common.dto.UpdatedSeatNumberDto;
import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.domain.reservation.common.exception.InvalidReservationTimeException;
import com.erp.erp.domain.reservation.common.exception.InvalidSeatRangeException;
import com.erp.erp.domain.reservation.common.exception.NoAvailableSeatException;
import com.erp.erp.domain.reservation.common.exception.NotFoundReservationException;
import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.response.ApiResult;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
      LocalDateTime startTime, LocalDateTime endTime) {
    Reservation reservation = ReservationGenerator.get(customer, institute, startTime, endTime);
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
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime =
        (RandomValue.getInt(0, 2) == 0) ? now.withMinute(0) : now.withMinute(30);

    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    AddReservationDto.Request request = AddReservationDto.Request.builder()
        .customerId(customer.getId())
        .startTime(startTime)
        .endTime(endTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .build();

    String url = BASE_URL + "/addReservation";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getReservationId()).isNotNull();
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(
        request.getStartTime().withSecond(0).withNano(0));
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(
        request.getEndTime().withSecond(0).withNano(0));
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(request.getSeatNumber());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
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

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddReservationDto.Response>>() {
        }.getType()
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
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime =
        (RandomValue.getInt(0, 2) == 0) ? now.withMinute(0) : now.withMinute(30);

    LocalDateTime endTime = startTime.minusMinutes(30 * RandomValue.getInt(1, 10));

    AddReservationDto.Request request = AddReservationDto.Request.builder()
        .customerId(customer.getId())
        .startTime(startTime)
        .endTime(endTime)
        .seatNumber(RandomValue.getInt(1, institute.getTotalSeat()))
        .build();

    InvalidReservationTimeException exception = new InvalidReservationTimeException();

    String url = BASE_URL + "/addReservation";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddReservationDto.Response>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getData()).isNull();
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("addReservations 좌석이 없을 경우")
  void addReservations_fail_3() {
    // given
    int totalSeat = RandomValue.getInt(1,5);
    Institute institute = createInstitutes(totalSeat);
    Customer customer = createCustomers(institute);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startTime =
        (RandomValue.getInt(0, 2) == 0) ? now.withMinute(0) : now.withMinute(30);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    IntStream.range(0, institute.getTotalSeat())
        .forEach(i -> createReservation(customer, institute, startTime, endTime));

    AddReservationDto.Request request = AddReservationDto.Request.builder()
        .customerId(customer.getId())
        .startTime(startTime)
        .endTime(endTime)
        .seatNumber(RandomValue.getInt(1, institute.getTotalSeat()))
        .build();

    NoAvailableSeatException exception = new NoAvailableSeatException();

    String url = BASE_URL + "/addReservation";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<AddReservationDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddReservationDto.Response>>() {
        }.getType()
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
    int hour = RandomValue.getInt(24);
    int randomInt = RandomValue.getInt(2);
    int minute = (randomInt == 1) ? 0 : 30;
    LocalTime randomTime = LocalTime.of(hour, minute);
    LocalDateTime startTime = LocalDateTime.of(day, randomTime);

    List<Reservation> reservations = IntStream.range(0, reservationsCount).mapToObj(i -> {
      LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
      return createReservation(customer, institute, startTime, endTime);
    }).toList();

    int nonReturnReservationCount = RandomValue.getInt(0, 5);
    Institute nonReturnInstitute = createInstitutes();
    Customer nonReturnCustomer = createCustomers(nonReturnInstitute);
    IntStream.range(0, nonReturnReservationCount).forEach(i -> {
      LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
      createReservation(nonReturnCustomer, nonReturnInstitute, startTime, endTime);
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
      assertThat(apiResponse.getData().get(i).getStartTime()).isEqualTo(reservations.get(i).getStartTime());
      assertThat(apiResponse.getData().get(i).getEndTime()).isEqualTo(reservations.get(i).getEndTime());
      assertThat(apiResponse.getData().get(i).getSeatNumber()).isEqualTo(reservations.get(i).getSeatNumber());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(reservations.get(i).getCustomer().getName());
    });
  }

  @Test
  @DisplayName("getReservationByTime 성공")
  void getReservationByTime() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    LocalDate day = RandomValue.getRandomLocalDate();
    int reservationsCount = RandomValue.getInt(0, 5);

    int hour = RandomValue.getInt(24);
    int minute = (RandomValue.getInt(2) == 1) ? 0 : 30;
    LocalTime randomTime = LocalTime.of(hour, minute);
    LocalDateTime startTime = LocalDateTime.of(day, randomTime);

    List<Reservation> reservations = IntStream.range(0, reservationsCount).mapToObj(i -> {
      LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
      return createReservation(customer, institute, startTime, endTime);
    }).toList();

    int nonReturnReservationCount = RandomValue.getInt(0, 5);
    Institute nonReturnInstitute = createInstitutes();
    Customer nonReturnCustomer = createCustomers(nonReturnInstitute);
    IntStream.range(0, nonReturnReservationCount).forEach(i -> {
      LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
      createReservation(nonReturnCustomer, nonReturnInstitute, startTime, endTime);
    });

    String url = BASE_URL + "/getReservationByTime?time=" + startTime;

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

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData().size()).isEqualTo(reservationsCount);
    IntStream.range(0, reservationsCount).forEach(i -> {
      assertThat(apiResponse.getData().get(i).getStartTime()).isEqualTo(reservations.get(i).getStartTime());
      assertThat(apiResponse.getData().get(i).getEndTime()).isEqualTo(reservations.get(i).getEndTime());
      assertThat(apiResponse.getData().get(i).getSeatNumber()).isEqualTo(reservations.get(i).getSeatNumber());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(reservations.get(i).getCustomer().getName());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(reservations.get(i).getCustomer().getName());
    });
  }

  @Test
  @DisplayName("updatedReservation 성공")
  void updatedReservation() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(0, 5);
    List<Progress> progressList = createProgressList(customer, progressSize);

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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
    assertThat(apiResponse.getData().getReservationId()).isEqualTo(request.getReservationId());
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime());
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(request.getSeatNumber());
    assertThat(apiResponse.getData().getAttendanceStatus()).isEqualTo(request.getAttendanceStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
    assertThat(actualProgress.size()).isEqualTo(progressSize);

    IntStream.range(0, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getProgressId()).isEqualTo(progressList.get(i).getId());
          assertThat(actualProgress.get(i).getContent()).isEqualTo(progressList.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(progressList.get(i).getDate());
        });
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

    int addProgressSize = RandomValue.getInt(1,5);
    List<AddProgress> addProgress = fixtureMonkey.giveMeBuilder(ProgressDto.AddProgress.class).sampleList(addProgressSize);

    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(addProgress)
        .updateProgresses(new ArrayList<>())
        .deleteProgresses(new ArrayList<>())
        .build();

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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
    assertThat(apiResponse.getData().getReservationId()).isEqualTo(request.getReservationId());
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime());
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(request.getSeatNumber());
    assertThat(apiResponse.getData().getAttendanceStatus()).isEqualTo(request.getAttendanceStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
    assertThat(actualProgress.size()).isEqualTo(addProgressSize + progressSize);

    IntStream.range(0, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getProgressId()).isEqualTo(progressList.get(i).getId());
          assertThat(actualProgress.get(i).getContent()).isEqualTo(progressList.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(progressList.get(i).getDate());
        });
    IntStream.range(addProgressSize+progressSize, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getContent()).isEqualTo(addProgress.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(addProgress .get(i).getDate());
        });
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
    List<Long> progressIds = new ArrayList<>(progressList.stream().map(Progress::getId).toList());
    Collections.shuffle(progressIds);

    List<UpdateProgress> updateProgress = IntStream.range(0, progressSize)
        .mapToObj(i -> {
          return UpdateProgress.builder()
              .progressId(progressIds.get(i))
              .date(RandomValue.getRandomLocalDate())
              .content(RandomValue.string(5).setNullable(false).get())
              .build();
        }).toList();

    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(new ArrayList<>())
        .updateProgresses(updateProgress)
        .deleteProgresses(new ArrayList<>())
        .build();

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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
    assertThat(apiResponse.getData().getReservationId()).isEqualTo(request.getReservationId());
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime());
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(request.getSeatNumber());
    assertThat(apiResponse.getData().getAttendanceStatus()).isEqualTo(request.getAttendanceStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
    actualProgress.sort(Comparator.comparing(ProgressResponse::getProgressId).reversed());
    List<UpdateProgress> updateProgressList = new ArrayList<>(updateProgress);
    updateProgressList.sort(Comparator.comparing(UpdateProgress::getProgressId).reversed());

    assertThat(actualProgress).hasSameSizeAs(updateProgress);
    IntStream.range(0, actualProgress.size())
        .forEach(i -> {
          assertThat(actualProgress.get(i).getProgressId()).isEqualTo(updateProgressList.get(i).getProgressId());
          assertThat(actualProgress.get(i).getContent()).isEqualTo(updateProgressList.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(updateProgressList.get(i).getDate());
        });

  }

  @Test
  @DisplayName("updatedReservation 진도표 삭제 성공")
  void updatedReservation_success_3() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomers(institute);
    int progressSize = RandomValue.getInt(1, 5);
    List<Progress> progressList = createProgressList(customer, progressSize);
    List<Long> progressIds = new ArrayList<>(progressList.stream().map(Progress::getId).toList());
    Collections.shuffle(progressIds);

    int deleteProgressSize = RandomValue.getInt(1, progressSize);
    List<DeleteProgress> deleteProgress = IntStream.range(0, deleteProgressSize)
        .mapToObj(i -> {
          return DeleteProgress.builder()
              .progressId(progressIds.get(i))
              .build();
        }).toList();

    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(new ArrayList<>())
        .updateProgresses(new ArrayList<>())
        .deleteProgresses(deleteProgress)
        .build();

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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
    assertThat(apiResponse.getData().getReservationId()).isEqualTo(request.getReservationId());
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(request.getStartTime());
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(request.getEndTime());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().getSeatNumber()).isEqualTo(request.getSeatNumber());
    assertThat(apiResponse.getData().getAttendanceStatus()).isEqualTo(request.getAttendanceStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();

    assertThat(actualProgress.size()).isEqualTo(progressSize-deleteProgressSize);
    for (ProgressResponse progressResponse : actualProgress) {
      for (DeleteProgress delete : deleteProgress) {
        assertThat(progressResponse.getProgressId()).isNotEqualTo(delete.getProgressId());
      }
    }

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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, nonReturnInstitute ,startTime, endTime);

    int seatNumber = RandomValue.getInt(1, nonReturnInstitute.getTotalSeat());
    UpdatedReservationDto.Request request = fixtureMonkey.giveMeBuilder(UpdatedReservationDto.Request.class)
        .set("reservationId", reservation.getId())
        .set("seatNumber", seatNumber)
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
    Reservation reservation = createReservation(customer, institute, LocalDateTime.now(), LocalDateTime.now());

    int seatNumber = RandomValue.getInt(0,2) == 0 ? 0 : institute.getTotalSeat() + RandomValue.getInt(1,5);
    UpdatedReservationDto.Request request = fixtureMonkey.giveMeBuilder(UpdatedReservationDto.Request.class)
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

    int updateProgressSize = RandomValue.getInt(1,5);
    List<UpdateProgress> updateProgress = IntStream.range(0, updateProgressSize)
        .mapToObj(i -> {
          long newId = Stream.generate(() -> RandomValue.getRandomLong(0, 99999))
              .filter(id -> !progressIds.contains(id))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("새로운 ID를 생성할 수 없습니다."));

          return UpdateProgress.builder()
              .progressId(newId)
              .date(RandomValue.getRandomLocalDate())
              .content(RandomValue.string(5).setNullable(false).get())
              .build();
        }).toList();


    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(new ArrayList<>())
        .updateProgresses(updateProgress)
        .deleteProgresses(new ArrayList<>())
        .build();

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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

    int deleteProgressSize = RandomValue.getInt(1,5);
    List<DeleteProgress> deleteProgresses = IntStream.range(0, deleteProgressSize)
        .mapToObj(i -> {
          long newId = Stream.generate(() -> RandomValue.getRandomLong(0, 99999))
              .filter(id -> !progressIds.contains(id))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("새로운 ID를 생성할 수 없습니다."));

          return DeleteProgress.builder()
              .progressId(newId)
              .build();
        }).toList();


    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(new ArrayList<>())
        .updateProgresses(new ArrayList<>())
        .deleteProgresses(deleteProgresses)
        .build();

    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute, startTime, endTime);

    int seatNumber = RandomValue.getInt(1, institute.getTotalSeat());
    LocalDateTime updateStartTime = RandomValue.getRandomLocalDateTime().withMinute(minute).withSecond(0);
    LocalDateTime updateEndTime = updateStartTime.plusMinutes(30 * RandomValue.getInt(1, 10));

    UpdatedReservationDto.Request request = UpdatedReservationDto.Request.builder()
        .reservationId(reservation.getId())
        .startTime(updateStartTime)
        .endTime(updateEndTime)
        .memo(RandomValue.string(255).get())
        .seatNumber(seatNumber)
        .attendanceStatus(RandomValue.getRandomEnum(AttendanceStatus.class))
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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute ,startTime, endTime);

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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, nonReturnInstitute ,startTime, endTime);

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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute ,startTime, endTime);


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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute ,startTime, endTime);

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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, nonReturnInstitute ,startTime, endTime);

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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, institute ,startTime, endTime);

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
    assertThat(apiResponse.getData().getStartTime()).isEqualTo(reservation.getStartTime());
    assertThat(apiResponse.getData().getEndTime()).isEqualTo(reservation.getEndTime());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(customer.getName());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(customer.getPhone());
    assertThat(apiResponse.getData().getPlanName()).isEqualTo(plan.getName());
    assertThat(apiResponse.getData().getEndDate()).isEqualTo(endDate);
//    assertThat(apiResponse.getData().getRemainingTime())
//    assertThat(apiResponse.getData().getUsedTime())
    assertThat(apiResponse.getData().getMemo()).isEqualTo(customer.getMemo());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
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
    int minute = RandomValue.getInt(0,2) == 1 ? 0 : 30;
    LocalDateTime startTime = RandomValue.getRandomLocalDateTime().withMinute(minute);
    LocalDateTime endTime = startTime.plusMinutes(30 * RandomValue.getInt(1, 10));
    Reservation reservation = createReservation(customer, nonReturnInstitute ,startTime, endTime);

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