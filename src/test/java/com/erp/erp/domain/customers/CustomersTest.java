package com.erp.erp.domain.customers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.accounts.business.PhotoUtil;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;

import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.repository.CustomersRepository;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
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



  @MockBean
  private PhotoUtil photoUtil;

  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Customers getCustomers() {
    Institutes institutes = createInstitutes();

    return fixtureMonkey.giveMeBuilder(Customers.class)
        .setNull("id")
        .set("institutesId", institutes)
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
}