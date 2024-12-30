package com.erp.erp.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto.ProgressResponse;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.exception.NotFoundCustomerException;
import com.erp.erp.domain.customer.repository.CustomerRepository;
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
import com.erp.erp.global.util.randomValue.Language;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
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

import java.util.List;
import java.util.stream.IntStream;

class CustomerTest extends IntegrationTest {

  int PAGE_LIMIT = 4;

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



  // note. 이후 이미지 처리 필요
  @Test
  @DisplayName("addCustomer 성공")
  void addCustomer() {
    //given
    Plan plan = createPlans();
    AddCustomerDto.Request request = fixtureMonkey.giveMeBuilder(AddCustomerDto.Request.class)
        .set("planId", plan.getId())
        .sample();

    String url = BASE_URL + "/addCustomer";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<AddCustomerDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddCustomerDto.Response>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(apiResponse.getData().getName()).isEqualTo(request.getName());
    assertThat(apiResponse.getData().getGender()).isEqualTo(request.getGender());
    assertThat(apiResponse.getData().getPhone()).isEqualTo(request.getPhone());
    assertThat(apiResponse.getData().getAddress()).isEqualTo(request.getAddress());
    assertThat(apiResponse.getData().getMemo()).isEqualTo(request.getMemo());
//    assertThat(apiResponse.getData().getPhotoUrl()).isEqualTo(photoUrl);
    assertThat(apiResponse.getData().getBirthDate()).isEqualTo(request.getBirthDate());
  }

  @Test
  @DisplayName("addCustomer 필수 값 미전달")
  void addCustomer_fail_1() {
    //given
    AddCustomerDto.Request request = AddCustomerDto.Request.builder().build();

    String url = BASE_URL + "/addCustomer";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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

    String url = BASE_URL + "/addCustomer";

    NotFoundPlanException exception = new NotFoundPlanException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url,
            request,
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

    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
            .set("customerId", customer.getId())
            .sample();

    String url = BASE_URL + "/updateCustomer";

    // then
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            url,
            request,
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

    List<ProgressResponse> actualProgress = apiResponse.getData().getProgress();
    List<ProgressResponse> expectedProgress = request.getProgress();
    assertThat(actualProgress).hasSameSizeAs(expectedProgress);
    IntStream.range(0, actualProgress.size())
            .forEach(i -> assertThat(actualProgress.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(expectedProgress.get(i)));

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

    // then
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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

    // then
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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

    // then
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        requestEntity,
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


    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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


    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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

    NotFoundCustomerException exception = new NotFoundCustomerException();

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
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
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
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

    IntStream.range(0 , dataSize).forEach(i -> {
      GetCustomerDto.Response response = apiResponse.getData().get(i);
      int index = randomInt - dataSize + i;
      Customer customer = customers.get(index);

      assertThat(response.getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
      assertThat(response.getName()).isEqualTo(customer.getName());
      assertThat(response.getGender()).isEqualTo(customer.getGender());
      assertThat(response.getPhone()).isEqualTo(customer.getPhone());
      assertThat(response.getLicenseType()).isEqualTo(customer.getPlanPayment().getPlan().getLicenseType());
      assertThat(response.getPlanName()).isEqualTo(customer.getPlanPayment().getPlan().getName());
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

    IntStream.range(0 , dataSize).forEach(i -> {
      GetCustomerDto.Response response = apiResponse.getData().get(i);
      int index = randomInt - dataSize + i;
      Customer customer = customers.get(index);

      assertThat(response.getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
      assertThat(response.getName()).isEqualTo(customer.getName());
      assertThat(response.getGender()).isEqualTo(customer.getGender());
      assertThat(response.getPhone()).isEqualTo(customer.getPhone());
      assertThat(response.getLicenseType()).isEqualTo(customer.getPlanPayment().getPlan().getLicenseType());
      assertThat(response.getPlanName()).isEqualTo(customer.getPlanPayment().getPlan().getName());
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
    assertThat(apiResponse.getData().getAddress()).isEqualTo(customer.getAddress());
//    assertThat(apiResponse.getData().getVisitPath()).isEqualTo(null);
    assertThat(apiResponse.getData().getMemo()).isEqualTo(customer.getMemo());
//    assertThat(apiResponse.getData().getProgressList()).isEqualTo(null);

    PlanPaymentResponse planPaymentResponse = apiResponse.getData().getPlanPayment();
    PlanPayment planPayment = customer.getPlanPayment();
    int planPrice = planPayment.getPlan().getPrice();
    int discountPrice = (int) (planPrice * planPayment.getDiscountRate());
    int paymentTotal = planPrice - discountPrice;
    assertThat(planPaymentResponse.getLicenseType()).isEqualTo(planPayment.getPlan().getLicenseType());
    assertThat(planPaymentResponse.getPlanName()).isEqualTo(planPayment.getPlan().getName());
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