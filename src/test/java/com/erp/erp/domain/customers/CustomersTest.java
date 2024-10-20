package com.erp.erp.domain.customers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.accounts.business.PhotoUtil;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.accounts.repository.AccountsRepository;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;

import com.erp.erp.domain.customers.common.dto.GetCustomerDto.Response;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
  private AccountsRepository accountsRepository;

  @MockBean
  private PhotoUtil photoUtil;


  private Accounts getAccounts() {
    Institutes institutes = createInstitutes();
    return fixtureMonkey.giveMeBuilder(Accounts.class)
        .setNull("id")
        .set("institutes", institutes)
        .sample();
  }
  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Accounts createAccounts() { return accountsRepository.save(getAccounts()); }
  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Customers getCustomers() {
    Institutes institutes = createInstitutes();

    return fixtureMonkey.giveMeBuilder(Customers.class)
        .setNull("id")
        .set("institutes", institutes)
        .sample();
  }


  // note. 이후 이미지 처리 필요
  @Test
  void addCustomer_성공() {
    //given
    AddCustomerDto.Request request = fixtureMonkey.giveMeBuilder(AddCustomerDto.Request.class)
        .set("gender","M")
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
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
  }


  @Test
  void updateStatus_성공() {
    //given

    Customers customers = getCustomers();
    customersRepository.save(customers);

    boolean status = !customers.getStatus();

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


  @Test
  void getCurrentCustomers_4명_반환() {
    //given
    Accounts accounts = createAccounts();
    Institutes institutes = accounts.getInstitutes();

    int randomInt = RandomValue.getInt(4,20);
    int page = Math.max(0, (randomInt / 4) - 1 );

    for(int i = 0; i<randomInt; i++) {
      Customers customers = fixtureMonkey.giveMeBuilder(Customers.class)
          .setNull("id")
          .set("institutes" , institutes)
          .set("status", true)
          .sample();
      customersRepository.save(customers);
    }

    String url = "http://localhost:" + port + "/api/customers/currentCustomers/" + page;


    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<Response>>>(){}.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    int dataSize = apiResponse.getData().size();
    assertThat(dataSize).isEqualTo(4);
  }

}