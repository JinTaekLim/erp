package com.erp.erp.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.account.business.PhotoUtil;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;

import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.PlanPaymentResponse;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.randomValue.Language;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

class CustomerTest extends IntegrationTest {


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

  @MockBean
  private PhotoUtil photoUtil;


  private Account getAccounts() {
    Institute institute = createInstitutes();
    return fixtureMonkey.giveMeBuilder(Account.class)
        .setNull("id")
        .set("institute", institute)
        .sample();
  }
  private Institute getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institute.class)
        .setNull("id")
        .sample();
  }

  private Account createAccounts() { return accountRepository.save(getAccounts()); }

  private Institute createInstitutes() {
    return instituteRepository.save(getInstitutes());
  }

  private Plan createPlans() {
    return planRepository.save(getPlans());
  }

  private Customer getCustomers(Institute institute, PlanPayment planPayment, List<OtherPayment> otherPaymentList) {
    return fixtureMonkey.giveMeBuilder(Customer.class)
            .setNull("id")
            .set("institute", institute)
            .set("planPayment", planPayment)
            .set("otherPayments", otherPaymentList)
            .set("progress", null)
            .sample();
  }

  private Customer createCustomers(Plan plan, Institute institute) {
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayment> otherPaymentList = getRandomOtherPaymentList(plan);
    Customer customer = getCustomers(institute, planPayment, otherPaymentList);
    return customerRepository.save(customer);
  }

  private Customer createCustomers(Institute institute, Plan plan, CustomerStatus status, String name) {
    PlanPayment planPayment = getPlanPayment(plan);
    List<OtherPayment> otherPaymentList = getRandomOtherPaymentList(plan);
    Customer customer = fixtureMonkey.giveMeBuilder(Customer.class)
        .setNull("id")
        .set("institute", institute)
        .set("planPayment", planPayment)
        .set("otherPayments", otherPaymentList)
        .set("progress", null)
        .set("status", status)
        .set("name", name)
        .sample();
    customerRepository.save(customer);
    return customer;
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
  private List<OtherPayment> getRandomOtherPaymentList(Plan plan) {
    int randomInt = RandomValue.getInt(0, 5);
    return fixtureMonkey.giveMeBuilder(OtherPayment.class)
            .setNull("id")
            .set("plan", plan)
            .sampleList(randomInt);
  }


  // note. 이후 이미지 처리 필요
  @Test
  void addCustomer_성공() {
    //given
    Plan plan = createPlans();
    AddCustomerDto.Request request = fixtureMonkey.giveMeBuilder(AddCustomerDto.Request.class)
        .set("gender","M")
        .set("planId", plan.getId())
        .sample();

    String url = BASE_URL + "/addCustomer";


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

    String url = BASE_URL + "/addCustomer";


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
            .planId(plan.getId())
            .name("name")
            .gender("M")
            .phone("01024214214")
            .address("adress")
            .birthDate(LocalDate.now())
            .build();

    String url = BASE_URL + "/addCustomer";


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
    Institute institute = createInstitutes();

    Customer customer = createCustomers(plan, institute);
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
    Institute institute = createInstitutes();
    Customer customer = createCustomers(plan, institute);

    CustomerStatus status = Arrays.stream(CustomerStatus.values())
        .filter(s -> s != customer.getStatus())
        .findAny()
        .orElseThrow(IllegalStateException::new);

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

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(customer.getStatus()).isNotEqualTo(status);
  }


  @Test()
  void searchCustomerName_성공() {
    //given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    CustomerStatus status = ( RandomValue.getInt(0,2) == 0 ) ? CustomerStatus.INACTIVE : CustomerStatus.ACTIVE;
    String name = RandomValue.string(10,20).setNullable(false).setLanguages(Language.ENGLISH).get();
    Customer customer = createCustomers(institute, plan, status, name);
////    String keyword = ( name.length() < 2 ) ? name : String.valueOf(name.charAt(0));


    String url = BASE_URL + "/searchCustomerName/" + customer.getName();


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

  @Test
  void getCustomerDetail() {
    // given
    Plan plan = createPlans();
    Institute institute = createInstitutes();
    Customer customer = createCustomers(plan, institute);

    String url = BASE_URL + "/getCustomerDetail/" + customer.getId();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
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
      OtherPaymentResponse response = apiResponse.getData().getOtherPayment().get(i);
      OtherPayment otherPayment = customer.getOtherPayments().get(i);
      assertThat(response.getRegistrationAt()).isEqualTo(otherPayment.getRegistrationAt());
      assertThat(response.getContent()).isEqualTo(otherPayment.getContent());
      assertThat(response.getPrice()).isEqualTo(otherPayment.getPrice());
      assertThat(response.isStatus()).isEqualTo(otherPayment.isStatus());
    }
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