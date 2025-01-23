package com.erp.erp.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.account.business.PhotoUtil;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;

import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.PlanPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto.AddProgress;
import com.erp.erp.domain.customer.common.dto.ProgressDto.DeleteProgress;
import com.erp.erp.domain.customer.common.dto.ProgressDto.ProgressResponse;
import com.erp.erp.domain.customer.common.dto.ProgressDto.UpdateProgress;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.common.exception.NotFoundProgressException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.customer.repository.ProgressRepository;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto.Request;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.exception.NotFoundPlanException;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.generator.AccountGenerator;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.generator.ProgressGenerator;
import com.erp.erp.global.util.randomValue.Language;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.IntStream;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

class CustomerTest extends IntegrationTest {

  int PAGE_LIMIT = 20;

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/customer";
  }

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private InstituteRepository instituteRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PlanRepository planRepository;

  @Autowired
  private ProgressRepository progressRepository;

  @Autowired
  private TokenManager tokenManager;

  @MockBean
  private PhotoUtil photoUtil;


  private Account createAccount(Institute institute) {
    return accountRepository.save(AccountGenerator.get(institute));
  }

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Plan createPlans() {
    return planRepository.save(PlanGenerator.get());
  }


  private Customer createCustomer() {
    Institute institute = createInstitutes();
    Plan plan = createPlans();
    return customerRepository.save(CustomerGenerator.get(plan, institute));
  }

  private Customer createCustomer(Institute institute) {
    Plan plan = createPlans();
    return customerRepository.save(CustomerGenerator.get(plan, institute));
  }

  private Customer createCustomer(Plan plan, Institute institute) {
    return customerRepository.save(CustomerGenerator.get(plan, institute));
  }

  private Customer createCustomer(Plan plan, Institute institute, CustomerStatus customerStatus) {
    Customer customer = CustomerGenerator.get(plan, institute, customerStatus);
    return customerRepository.save(customer);
  }

  private Customer createCustomer(Institute institute, Plan plan, CustomerStatus status, String name) {
    Customer customer = CustomerGenerator.get(plan, institute, status, name);
    customerRepository.save(customer);
    return customer;
  }

  private List<Progress> createProgressList(Customer customer, int size) {
    List<Progress> progressList = ProgressGenerator.get(customer, size);
    progressRepository.saveAll(progressList);
    return progressList;
  }


  @Test
  @DisplayName("addCustomer 성공")
  void addCustomer() {
    //given
    Plan plan = createPlans();
    AddCustomerDto.Request request = fixtureMonkey.giveMeBuilder(AddCustomerDto.Request.class)
        .set("planId", plan.getId())
        .sample();

    MockMultipartFile mockFile = new MockMultipartFile(
        "file",
        "test-image.jpg",
        "image/jpeg",
        "test-image-content".getBytes());

    String photoUrl = RandomValue.string(5,30).setNullable(false).get();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("req", new HttpEntity<>(gson.toJson(request), partHeaders));
    body.add("file", mockFile.getResource());

    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, requestHeaders);


    String url = BASE_URL + "/addCustomer";

    //when
    when(photoUtil.upload(any(MultipartFile.class))).thenReturn(photoUrl);

    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        httpEntity,
        String.class
    );

    ApiResult<AddCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddCustomerDto.Response>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(apiResponse.getData().getPlanName()).isEqualTo(plan.getName());
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getVisitPath()).isEqualTo(request.getVisitPath());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(photoUrl);
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
  }

  @Test
  @DisplayName("addCustomer 필수 값 미전달")
  void addCustomer_fail_1() {
    //given
    AddCustomerDto.Request request = AddCustomerDto.Request.builder().build();

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("req", new HttpEntity<>(gson.toJson(request), partHeaders));


    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, requestHeaders);

    String url = BASE_URL + "/addCustomer";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        httpEntity,
        String.class
    );

    ApiResult<AddCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddCustomerDto.Response>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNotNull(apiResponse);
  }

  @Test
  @DisplayName("addCustomer 존재하지 않는 이용권 전달")
  void addCustomer_fail_2() {
    //given
    AddCustomerDto.Request request = fixtureMonkey.giveMeOne(AddCustomerDto.Request.class);

    HttpHeaders partHeaders = new HttpHeaders();
    partHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("req", new HttpEntity<>(gson.toJson(request), partHeaders));


    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, requestHeaders);

    String url = BASE_URL + "/addCustomer";

    NotFoundPlanException exception = new NotFoundPlanException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url,
            httpEntity,
            String.class
    );

    ApiResult<AddCustomerDto.Response> apiResponse = gson.fromJson(
            responseEntity.getBody(),
            new TypeToken<ApiResult<AddCustomerDto.Response>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNotNull(apiResponse);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updateCustomer 성공")
  void updateCustomer() {
    // given
    Customer customer = createCustomer();

    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(new ArrayList<>())
        .updateProgresses(new ArrayList<>())
        .deleteProgresses(new ArrayList<>())
        .build();

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progressList", progressRequest)
        .sample();

    String url = BASE_URL + "/updateCustomer";
    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            httpRequest,
            String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
            responseEntity.getBody(),
            new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getCustomerId()).isEqualTo(request.getCustomerId());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(request.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().isPlanPaymentStatus()).isEqualTo(request.isPlanPaymentStatus());

    List<OtherPaymentResponse> actualPayments = apiResponse.getData().getOtherPayment();
    List<OtherPaymentResponse> expectedPayments = request.getOtherPayment();
    assertThat(actualPayments).hasSameSizeAs(expectedPayments);
    IntStream.range(0, actualPayments.size())
            .forEach(i -> assertThat(actualPayments.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(expectedPayments.get(i)));
  }

  @Test()
  @DisplayName("updateCustomer 진도표 추가 성공")
  void updateCustomer_success_1() {
    // given
    Customer customer = createCustomer();
    int progressSize = RandomValue.getInt(1,5);
    List<Progress> progresses = createProgressList(customer, progressSize);

    int addProgressSize = RandomValue.getInt(1,5);
    List<AddProgress> addProgress = fixtureMonkey.giveMeBuilder(ProgressDto.AddProgress.class).sampleList(addProgressSize);

    ProgressDto.Request progressRequest = ProgressDto.Request.builder()
        .addProgresses(addProgress)
        .updateProgresses(new ArrayList<>())
        .deleteProgresses(new ArrayList<>())
        .build();

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progressList", progressRequest)
        .sample();

    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getCustomerId()).isEqualTo(request.getCustomerId());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(request.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().isPlanPaymentStatus()).isEqualTo(request.isPlanPaymentStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
    assertThat(actualProgress.size()).isEqualTo(addProgressSize+progressSize);

    IntStream.range(0, progressSize)
            .forEach(i -> {
              assertThat(actualProgress.get(i).getContent()).isEqualTo(progresses.get(i).getContent());
              assertThat(actualProgress.get(i).getDate()).isEqualTo(progresses.get(i).getDate());
            });
    IntStream.range(addProgressSize+progressSize, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getContent()).isEqualTo(addProgress.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(addProgress .get(i).getDate());
        });

    List<OtherPaymentResponse> actualPayments = apiResponse.getData().getOtherPayment();
    List<OtherPaymentResponse> expectedPayments = request.getOtherPayment();
    assertThat(actualPayments).hasSameSizeAs(expectedPayments);
    IntStream.range(0, actualPayments.size())
        .forEach(i -> assertThat(actualPayments.get(i))
            .usingRecursiveComparison()
            .isEqualTo(expectedPayments.get(i)));
  }

  @Test()
  @DisplayName("updateCustomer 진도표 수정 성공")
  void updateCustomer_success_2() {
    // given
    Customer customer = createCustomer();
    int progressSize = RandomValue.getInt(1,5);
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

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progressList", progressRequest)
        .sample();

    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getCustomerId()).isEqualTo(request.getCustomerId());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(request.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getVisitPath()).isEqualTo(request.getVisitPath());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().isPlanPaymentStatus()).isEqualTo(request.isPlanPaymentStatus());

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

    List<OtherPaymentResponse> actualPayments = apiResponse.getData().getOtherPayment();
    List<OtherPaymentResponse> expectedPayments = request.getOtherPayment();
    assertThat(actualPayments).hasSameSizeAs(expectedPayments);
    IntStream.range(0, actualPayments.size())
        .forEach(i -> assertThat(actualPayments.get(i))
            .usingRecursiveComparison()
            .isEqualTo(expectedPayments.get(i)));
  }

  @Test()
  @DisplayName("updateCustomer 진도표 삭제 성공")
  void updateCustomer_success_3() {
    // given
    Customer customer = createCustomer();
    int progressSize = RandomValue.getInt(1,5);
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

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progressList", progressRequest)
        .sample();

    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getCustomerId()).isEqualTo(request.getCustomerId());
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(request.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
    assertThat(apiResponse.getData().isPlanPaymentStatus()).isEqualTo(request.isPlanPaymentStatus());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();

    assertThat(actualProgress.size()).isEqualTo(progressSize-deleteProgressSize);
    for (ProgressResponse progressResponse : actualProgress) {
      for (DeleteProgress delete : deleteProgress) {
        assertThat(progressResponse.getProgressId()).isNotEqualTo(delete.getProgressId());
        }
      }


    List<OtherPaymentResponse> actualPayments = apiResponse.getData().getOtherPayment();
    List<OtherPaymentResponse> expectedPayments = request.getOtherPayment();
    assertThat(actualPayments).hasSameSizeAs(expectedPayments);
    IntStream.range(0, actualPayments.size())
        .forEach(i -> assertThat(actualPayments.get(i))
            .usingRecursiveComparison()
            .isEqualTo(expectedPayments.get(i)));
  }


  @Test
  @DisplayName("updateCustomer 필수 값 미입력")
  void updateCustomer_fail_1() {
    // given
    UpdateCustomerDto.Request request = UpdateCustomerDto.Request.builder().build();

    String url = BASE_URL + "/updateCustomer";
    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
  }

  @Test
  @DisplayName("updateCustomer 존재하지 않는 Customer 입력")
  void updateCustomer_fail_2() {
    // given
    UpdateCustomerDto.Request request = fixtureMonkey.giveMeOne(UpdateCustomerDto.Request.class);

    String url = BASE_URL + "/updateCustomer";

    NotFoundCustomerException exception = new NotFoundCustomerException();
    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


  @Test
  @DisplayName("updateCustomer 다른 매장 Customer 입력")
  void updateCustomer_fail_3() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomer();
    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .sample();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdateCustomerDto.Request> requestEntity = new HttpEntity<>(request, headers);

    String url = BASE_URL + "/updateCustomer";

    NotFoundCustomerException exception = new NotFoundCustomerException();
    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test()
  @DisplayName("updateCustomer 진도표 수정 잘못된 ID값 입력")
  void updateCustomer_fail_4() {
    // given
    Customer customer = createCustomer();
    int progressSize = RandomValue.getInt(0,2);
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

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progress", progressRequest)
        .sample();

    NotFoundProgressException exception = new NotFoundProgressException();

    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


  @Test()
  @DisplayName("updateCustomer 진도표 삭제 잘못된 ID값 입력")
  void updateCustomer_fail_5() {
    // given
    Customer customer = createCustomer();
    int progressSize = RandomValue.getInt(0,2);
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

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
        .set("customerId", customer.getId())
        .set("progress", progressRequest)
        .sample();

    NotFoundProgressException exception = new NotFoundProgressException();

    HttpEntity<UpdateCustomerDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<UpdateCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateCustomerDto.Response>>(){}
    );

    // when
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updateStatus 성공")
  void updateStatus() {
    //given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    Customer customer = createCustomer(plan, institute);

    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .customerId(customer.getId())
        .status(status)
        .build();

    String url = BASE_URL + "/updateStatus";
    HttpEntity<UpdateStatusDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<CustomerStatus> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<CustomerStatus>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(apiResponse.getData()).isEqualTo(status);
  }

  @Test
  @DisplayName("updateStatus status 미전달")
  void updateStatus_fail_1() {
    //given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    Customer customer = createCustomer(plan, institute);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .customerId(customer.getId())
        .build();

    String url = BASE_URL + "/updateStatus";
    HttpEntity<UpdateStatusDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
  }

  @Test
  @DisplayName("updateStatus customerId 미전달")
  void updateStatus_fail_2() {
    //given
    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .status(status)
        .build();

    String url = BASE_URL + "/updateStatus";
    HttpEntity<UpdateStatusDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
  }

  @Test
  @DisplayName("updateStatus 잘못된 customerId 전달")
  void updateStatus_fail_3() {
    //given
    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .customerId(RandomValue.getRandomLong(99999))
        .status(status)
        .build();

    String url = BASE_URL + "/updateStatus";
    HttpEntity<UpdateStatusDto.Request> httpRequest = new HttpEntity<>(request, new HttpHeaders());
    NotFoundCustomerException exception = new NotFoundCustomerException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        httpRequest,
        String.class
    );

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("updateStatus 다른 매장 customerId 전달")
  void updateStatus_fail_4() {
    //given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomer();
    CustomerStatus status = RandomValue.getRandomEnum(CustomerStatus.class);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .customerId(customer.getId())
        .status(status)
        .build();

    String url = BASE_URL + "/updateStatus";

    NotFoundCustomerException exception = new NotFoundCustomerException();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<UpdateStatusDto.Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        String.class
    );

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


  @Test
  @DisplayName("getCurrentCustomers 성공")
  void getCurrentCustomers() {
    //given
    Institute institutes = createInstitutes();
    Plan plan = createPlans();

    int randomInt = RandomValue.getInt(0,20);
    int page = randomInt / PAGE_LIMIT;

    List<Customer> customers = IntStream.range(0, randomInt)
        .mapToObj(i -> {
      return createCustomer(plan, institutes, CustomerStatus.ACTIVE);
        }).toList();

    String url = BASE_URL + "/currentCustomers/" + page;

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetCustomerDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetCustomerDto.Response>>>(){}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    int dataSize = randomInt - (page * PAGE_LIMIT);
    assertThat(apiResponse.getData().size()).isEqualTo(dataSize);

    IntStream.range(dataSize , 0).forEach(i -> {
      GetCustomerDto.Response response = apiResponse.getData().get(i);
      int index = randomInt - dataSize + i;
      Customer customer = customers.get(index);

      assertThat(response.getCustomerId()).isNotNull();
      assertThat(response.getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
      assertThat(response.getName()).isEqualTo(customer.getName());
      assertThat(response.getGender()).isEqualTo(customer.getGender());
      assertThat(response.getPhone()).isEqualTo(customer.getPhone());
      assertThat(response.getLicenseType()).isEqualTo(customer.getPlanPayment().getPlan().getLicenseType());
      assertThat(response.getPlanName()).isEqualTo(customer.getPlanPayment().getPlan().getName());
      assertThat(response.getCourseType()).isEqualTo(customer.getPlanPayment().getPlan().getCourseType());
//      assertThat(response.getRemainingTime())
//      assertThat(response.getRemainingPeriod())
//      assertThat(response.getUsedTime())
      assertThat(response.getRegistrationDate()).isEqualTo(customer.getPlanPayment().getRegistrationAt());
//      assertThat(response.getTardinessCount())
//      assertThat(response.getAbsenceCount())
    });
  }

  @Test
  @DisplayName("getExpiredCustomers 성공")
  void getExpiredCustomers() {
    //given
    Institute institutes = createInstitutes();
    Plan plan = createPlans();

    int randomInt = RandomValue.getInt(0,20);
    int page = randomInt / PAGE_LIMIT;

    List<Customer> customers = IntStream.range(0, randomInt)
        .mapToObj(i -> {
          return createCustomer(plan, institutes, CustomerStatus.INACTIVE);
        }).toList();

    String url = BASE_URL + "/expiredCustomer/" + page;

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetCustomerDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetCustomerDto.Response>>>(){}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    int dataSize = randomInt - (page * PAGE_LIMIT);
    assertThat(apiResponse.getData().size()).isEqualTo(dataSize);

    IntStream.range(dataSize , 0).forEach(i -> {
      GetCustomerDto.Response response = apiResponse.getData().get(i);
      int index = randomInt - dataSize + i;
      Customer customer = customers.get(index);

      assertThat(response.getCustomerId()).isNotNull();
      assertThat(response.getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
      assertThat(response.getName()).isEqualTo(customer.getName());
      assertThat(response.getGender()).isEqualTo(customer.getGender());
      assertThat(response.getPhone()).isEqualTo(customer.getPhone());
      assertThat(response.getLicenseType()).isEqualTo(customer.getPlanPayment().getPlan().getLicenseType());
      assertThat(response.getPlanName()).isEqualTo(customer.getPlanPayment().getPlan().getName());
      assertThat(response.getCourseType()).isEqualTo(customer.getPlanPayment().getPlan().getCourseType());
//      assertThat(response.getRemainingTime())
//      assertThat(response.getRemainingPeriod())
//      assertThat(response.getUsedTime())
      assertThat(response.getRegistrationDate()).isEqualTo(customer.getPlanPayment().getRegistrationAt());
//      assertThat(response.getTardinessCount())
//      assertThat(response.getAbsenceCount())
    });
  }

  @Test
  @DisplayName("getAvailableCustomerNames 성공")
  void getAvailableCustomerNames() {
    // given
    Institute institutes = createInstitutes();
    Plan plan = createPlans();

    int customerCount = RandomValue.getInt(0,5);
    List<Customer> customers = IntStream.range(0, customerCount)
        .mapToObj(i -> {
          return createCustomer(plan, institutes, CustomerStatus.ACTIVE);
        }).toList();

    int InactiveCustomerCount = RandomValue.getInt(0,5);
    IntStream.range(0, InactiveCustomerCount)
        .forEach(i -> createCustomer(plan, institutes, CustomerStatus.INACTIVE));

    int deletedCustomerCount = RandomValue.getInt(0,5);
    IntStream.range(0, deletedCustomerCount)
        .forEach(i -> createCustomer(plan, institutes, CustomerStatus.DELETED));


    String url = BASE_URL + "/getAvailableCustomerNames";

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetAvailableCustomerNamesDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetAvailableCustomerNamesDto.Response>>>(){}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData().size()).isEqualTo(customerCount);
    IntStream.range(0, customerCount).forEach(i -> {
      assertThat(apiResponse.getData().get(i).getId()).isEqualTo(customers.get(i).getId());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(customers.get(i).getName());
    });
  }

  @Test
  @DisplayName("searchCustomerName 성공")
  void searchCustomerName() {
    //given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    CustomerStatus status = ( RandomValue.getInt(0,2) == 0 ) ? CustomerStatus.INACTIVE : CustomerStatus.ACTIVE;
    String name = RandomValue.string(10,20).setNullable(false).setLanguages(Language.ENGLISH).get();
    Customer customer = createCustomer(institute, plan, status, name);
    String keyword = ( name.length() < 2 ) ? name : String.valueOf(name.charAt(0));

    String url = BASE_URL + "/searchCustomerName/" + keyword;

    //when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        String.class
    );
    ApiResult<List<SearchCustomerNameDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<SearchCustomerNameDto.Response>>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(apiResponse.getData().get(0).getCustomerId()).isEqualTo(customer.getId());
    assertThat(apiResponse.getData().get(0).getName()).isEqualTo(customer.getName());
    assertThat(apiResponse.getData().get(0).getStatus()).isEqualTo(status);
  }

  @Test
  @DisplayName("searchCustomerName 다른 매장 회원 검색")
  void searchCustomerName_2() {
    //given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);
    Customer customer = createCustomer();

    String url = BASE_URL + "/searchCustomerName/" + customer.getName();

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
    ApiResult<List<SearchCustomerNameDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<SearchCustomerNameDto.Response>>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData()).isEmpty();
  }

  @Test
  @DisplayName("getCustomerDetail 성공")
  void getCustomerDetail() {
    // given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    Customer customer = createCustomer(plan, institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    int progressSize = RandomValue.getInt(0,5);
    List<Progress> progressList = createProgressList(customer, progressSize);


    String url = BASE_URL + "/getCustomerDetail/" + customer.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());

    HttpEntity<Request> requestEntity = new HttpEntity<>(headers);

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetCustomerDetailDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetCustomerDetailDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData()).isNotNull();
    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
    assertThat(apiResponse.getData().getName()).isEqualTo(customer.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(customer.getGender());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(customer.getPhone());
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(customer.getBirthDate());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(customer.getAddress());
    assertThat(apiResponse.getData().getVisitPath()).isEqualTo(customer.getVisitPath());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(customer.getMemo());

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgressList();
    assertThat(actualProgress.size()).isEqualTo(progressSize);

    IntStream.range(0, progressSize)
        .forEach(i -> {
          assertThat(actualProgress.get(i).getProgressId()).isEqualTo(progressList.get(i).getId());
          assertThat(actualProgress.get(i).getContent()).isEqualTo(progressList.get(i).getContent());
          assertThat(actualProgress.get(i).getDate()).isEqualTo(progressList.get(i).getDate());
        });

    PlanPaymentResponse planPaymentResponse = apiResponse.getData().getPlanPayment();
    PlanPayment planPayment = customer.getPlanPayment();
    int planPrice = planPayment.getPlan().getPrice();
    int discountPrice = (int) (planPrice * planPayment.getDiscountRate());
    int paymentTotal = planPrice - discountPrice;
    assertThat(planPaymentResponse.getLicenseType()).isEqualTo(planPayment.getPlan().getLicenseType());
    assertThat(planPaymentResponse.getPlanName()).isEqualTo(planPayment.getPlan().getName());
    assertThat(planPaymentResponse.getCourseType()).isEqualTo(planPayment.getPlan().getCourseType());
    assertThat(planPaymentResponse.getPlanPrice()).isEqualTo(planPayment.getPlan().getPrice());
    assertThat(planPaymentResponse.getDiscountRate()).isEqualTo(planPayment.getDiscountRate());
    assertThat(planPaymentResponse.getDiscountPrice()).isEqualTo(discountPrice);
    assertThat(planPaymentResponse.getPaymentsMethod()).isEqualTo(planPayment.getPaymentsMethod());
    assertThat(planPaymentResponse.getRegistrationAt()).isEqualTo(planPayment.getRegistrationAt());
    assertThat(planPaymentResponse.getPaymentTotal()).isEqualTo(paymentTotal);
    assertThat(planPaymentResponse.isStatus()).isEqualTo(planPayment.isStatus());


    for (int i=0; i<apiResponse.getData().getOtherPayment().size(); i++) {
      GetCustomerDetailDto.OtherPaymentResponse response = apiResponse.getData().getOtherPayment().get(i);
      OtherPayment otherPayment = customer.getOtherPayments().get(i);
      assertThat(response.getRegistrationAt()).isEqualTo(otherPayment.getRegistrationAt());
      assertThat(response.getContent()).isEqualTo(otherPayment.getContent());
      assertThat(response.getPrice()).isEqualTo(otherPayment.getPrice());
      assertThat(response.isStatus()).isEqualTo(otherPayment.isStatus());
    }
  }



  @Test
  @DisplayName("getCustomerDetail 잘못된 customerId 전달")
  void getCustomerDetail_fail_1() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String customerId = String.valueOf(RandomValue.getRandomLong(999));


    String url = BASE_URL + "/getCustomerDetail/" + customerId;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Request> requestEntity = new HttpEntity<>(headers);

    NotFoundCustomerException exception = new NotFoundCustomerException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetCustomerDetailDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetCustomerDetailDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  @DisplayName("getCustomerDetail 다른 매장 회원 전달")
  void getCustomerDetail_fail_2() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    Customer customer = createCustomer();

    String url = BASE_URL + "/getCustomerDetail/" + customer.getId();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Request> requestEntity = new HttpEntity<>(headers);

    NotFoundCustomerException exception = new NotFoundCustomerException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        String.class
    );

    ApiResult<GetCustomerDetailDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetCustomerDetailDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


}