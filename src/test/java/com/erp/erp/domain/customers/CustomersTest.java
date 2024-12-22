package com.erp.erp.domain.customers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.account.business.PhotoUtil;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;

import com.erp.erp.domain.customers.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customers.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.CustomerStatus;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.entity.Progress;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.domain.payments.common.entity.OtherPayments;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.randomValue.Language;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

class CustomersTest extends IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CustomersRepository customersRepository;

  @Autowired
  private InstitutesRepository institutesRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PlanRepository planRepository;

  @MockBean
  private PhotoUtil photoUtil;


  private Account getAccounts() {
    Institutes institutes = createInstitutes();
    return fixtureMonkey.giveMeBuilder(Account.class)
        .setNull("id")
        .set("institutes", institutes)
        .sample();
  }
  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Account createAccounts() { return accountRepository.save(getAccounts()); }
  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Plan createPlans() {
    return planRepository.save(getPlans());
  }

  private Customers getCustomers(Institutes institutes, PlanPayment planPayment, List<OtherPayments> otherPaymentList) {
    return fixtureMonkey.giveMeBuilder(Customers.class)
            .setNull("id")
            .set("institutes", institutes)
            .set("planPayment", planPayment)
            .set("otherPayments", otherPaymentList)
            .set("progress", null)
            .sample();
  }

  private Customers createCustomers(Plan plan, Institutes institutes) {
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plan);
    Customers customers = getCustomers(institutes, planPayment, otherPaymentList);
    return customersRepository.save(customers);
  }

  private Customers createCustomers(Institutes institutes, Plan plan, CustomerStatus status, String name) {
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayments> otherPaymentList = getRandomOtherPaymentList(plan);
    Customers customers = fixtureMonkey.giveMeBuilder(Customers.class)
        .setNull("id")
        .set("institutes", institutes)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .set("progress", null)
        .set("status", status)
        .set("name", name)
        .sample();
    customersRepository.save(customers);
    return customers;
  }



  private Plan getPlans() {
    return fixtureMonkey.giveMeBuilder(Plan.class)
        .setNull("id")
        .sample();
  }

  private PlanPayment getPlanPayment(Plan plan) {
    return fixtureMonkey.giveMeBuilder(PlanPayment.class)
            .setNull("id")
            .set("plan", plan)
            .sample();
  }
  private List<OtherPayments> getRandomOtherPaymentList(Plan plan) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayments.class)
            .setNull("id")
            .set("plans", plan)
            .sampleList(randomInt);
  }


  // note. 이후 이미지 처리 필요
  @Test
  void addCustomer_성공() {
    //given
    Plan plan = createPlans();
    AddCustomerDto.Request request = fixtureMonkey.giveMeBuilder(AddCustomerDto.Request.class)
        .set("gender","M")
        .set("plansId", plan.getId())
        .sample();

    String url = "http://localhost:" + port + "/api/customers/addCustomer";


    //when
    when(photoUtil.upload(any())).thenReturn(RandomValue.string(50).get());

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
  }

   // note. 이후 이미지 처리 필요
  @Test
  void addCustomer_잘못된_성별() {
    //given
    AddCustomerDto.Request request = fixtureMonkey.giveMeOne(AddCustomerDto.Request.class);

    String url = "http://localhost:" + port + "/api/customers/addCustomer";


    //when
    when(photoUtil.upload(any())).thenReturn(RandomValue.string(50).get());

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
  void addCustomer_이용권_미입력() {
    //given
    Plan plan = createPlans();
    AddCustomerDto.Request request = AddCustomerDto.Request.builder()
            .plansId(plan.getId())
            .name("name")
            .gender("M")
            .phone("01024214214")
            .address("adress")
            .birthDate(LocalDate.now())
            .build();

    String url = "http://localhost:" + port + "/api/customers/addCustomer";


    //when
    when(photoUtil.upload(any())).thenReturn(RandomValue.string(50).get());

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
  }

  @Test
  void updateCustomer() {
    // given
    Plan plan = createPlans();
    Institutes institutes = createInstitutes();

    Customers customers = createCustomers(plan, institutes);
    UpdateCustomerDto.Request request = fixtureMonkey.giveMeBuilder(UpdateCustomerDto.Request.class)
            .set("customerId", customers.getId())
            .sample();

    String url = "http://localhost:" + port + "/api/customers/updateCustomer";

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

    List<Progress.ProgressItem> actualProgress = apiResponse.getData().getProgress();
    List<Progress.ProgressItem> expectedProgress = request.getProgress();
    assertThat(actualProgress).hasSameSizeAs(expectedProgress);
    IntStream.range(0, actualProgress.size())
            .forEach(i -> assertThat(actualProgress.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(expectedProgress.get(i)));



    List<UpdateCustomerDto.OtherPayment> actualPayments = apiResponse.getData().getOtherPayment();
    List<UpdateCustomerDto.OtherPayment> expectedPayments = request.getOtherPayment();
    assertThat(actualPayments).hasSameSizeAs(expectedPayments);
    IntStream.range(0, actualPayments.size())
            .forEach(i -> assertThat(actualPayments.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(expectedPayments.get(i)));



  }

  @Test
  void updateStatus_성공() {
    //given
    Plan plan = createPlans();
    Institutes institutes = createInstitutes();
    Customers customers = createCustomers(plan, institutes);

    CustomerStatus status = Arrays.stream(CustomerStatus.values())
        .filter(s -> s != customers.getStatus())
        .findAny()
        .orElseThrow(IllegalStateException::new);

    UpdateStatusDto.Request request = UpdateStatusDto.Request.builder()
        .customersId(customers.getId())
        .status(status)
        .build();


    String url = "http://localhost:" + port + "/api/customers/updateStatus";


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
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(customers.getStatus()).isNotEqualTo(status);
  }


  @Test()
  void searchCustomerName_성공() {
    //given
    Plan plan = createPlans();
    Institutes institutes = createInstitutes();
    CustomerStatus status = ( RandomValue.getInt(0,2) == 0 ) ? CustomerStatus.INACTIVE : CustomerStatus.ACTIVE;
    String name = RandomValue.string(10,20).setNullable(false).setLanguages(Language.ENGLISH).get();
    Customers customers = createCustomers(institutes, plan, status, name);
////    String keyword = ( name.length() < 2 ) ? name : String.valueOf(name.charAt(0));


    String url = "http://localhost:" + port + "/api/customers/searchCustomerName/" + customers.getName();


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
//    assertThat(apiResponse.getData().get(0).getCustomerId()).isEqualTo(customers.getId());
//    assertThat(apiResponse.getData().get(0).getName()).isEqualTo(customers.getName());
//    assertThat(apiResponse.getData().get(0).getStatus()).isEqualTo(status);
  }



//
//  @Test()
//  void getCurrentCustomers() {
//    //given
//    Accounts accounts = createAccounts();
//    Institutes institutes = accounts.getInstitutes();
//
//    Plan plan = createPlans();
//    PlanPayment planPayment = getPlanPayment(plan);
//    List<OtherPayments> otherPayments = getRandomOtherPaymentList(plan);
//
//    int randomInt = RandomValue.getInt(0,4);
//    int page = 0;
//
//    for(int i = 0; i<randomInt; i++) {
//      Customers customers = Customers.builder()
//          .institutes(institutes)
//          .name("name")
//          .gender(Gender.MALE)
//          .phone("phone")
//          .address("address")
//          .photoUrl("photoUrl")
//          .memo("memo")
//          .birthDate(LocalDate.now())
//          .planPayment(planPayment)
//          .otherPayments(otherPayments)
//          .build();
//      customersRepository.save(customers);
//    }
//
//    String url = "http://localhost:" + port + "/api/customers/currentCustomers/" + page;
//
//
//    // when
//    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
//        url,
//        String.class
//    );
//
//    ApiResult<List<GetCustomerDto.Response>> apiResponse = gson.fromJson(
//        responseEntity.getBody(),
//        new TypeToken<ApiResult<List<GetCustomerDto.Response>>>(){}.getType()
//    );
//
//    // then
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertNotNull(apiResponse);
//
//    int dataSize = apiResponse.getData().size();
//    assertThat(dataSize).isEqualTo(randomInt);
//  }
//
//
//
//  @Test
//  void getCurrentCustomers_4명_반환() {
//    //given
//    Accounts accounts = createAccounts();
//    Institutes institutes = accounts.getInstitutes();
//    Plan plan = createPlans();
//    PlanPayment planPayment = getPlanPayment(plan);
//    List<OtherPayments> otherPayments = getRandomOtherPaymentList(plan);
//
//    int randomInt = RandomValue.getInt(4,20);
//    int page = Math.max(0, (randomInt / 4) - 1 );
//
//
//    for(int i = 0; i<randomInt; i++) {
//      Customers customers = fixtureMonkey.giveMeBuilder(Customers.class)
//          .setNull("id")
//          .set("institutes" , institutes)
//          .set("status", CustomerStatus.ACTIVE)
//          .set("planPayment" , planPayment)
//          .set("otherPayments" , otherPayments)
//          .sample();
//      customersRepository.save(customers);
//    }
//
//
//    String url = "http://localhost:" + port + "/api/customers/currentCustomers/" + page;
//
//
//    // when
//    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
//        url,
//        String.class
//    );
//
//    ApiResult<List<GetCustomerDto.Response>> apiResponse = gson.fromJson(
//        responseEntity.getBody(),
//        new TypeToken<ApiResult<List<GetCustomerDto.Response>>>(){}.getType()
//    );
//
//    // then
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertNotNull(apiResponse);
//
//    int dataSize = apiResponse.getData().size();
//    assertThat(dataSize).isEqualTo(4);
//  }

}