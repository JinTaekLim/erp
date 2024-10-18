package com.erp.erp.domain.institutes;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.service.AuthService;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto.Request;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


public class InstitutesTest extends IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AuthService authService;

  @Test
  void updateTotalSpots_성공() {
    //given
    /* 인증 관련 코드 추가 작성 필요*/
    Accounts accounts = fixtureMonkey.giveMeOne(Accounts.class);
    
    Institutes institutes = accounts.getInstitute();

    UpdateTotalSpotsDto.Request request = fixtureMonkey.giveMeOne(
        UpdateTotalSpotsDto.Request.class
    );;
    String url = "http://localhost:" + port + "/api/institutes/updateTotalSpots";

    //when
    when(authService.getAccountsInfo()).thenReturn(accounts);


    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<UpdateTotalSpotsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSpotsDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    UpdateTotalSpotsDto.Response response = apiResponse.getData();
    assertThat(institutes.getId()).isEqualTo(response.getId());
    assertThat(institutes.getName()).isEqualTo(response.getName());
    assertThat(institutes.getTotalSpots()).isEqualTo(response.getNum());
  }


  @Test
  void updateTotalSpots_잘못된_Num값() {
    //given
    /* 인증 관련 코드 추가 작성 필요*/
    Accounts accounts = fixtureMonkey.giveMeOne(Accounts.class);

    Institutes institutes = accounts.getInstitute();

    UpdateTotalSpotsDto.Request request = UpdateTotalSpotsDto.Request.builder()
        .num(RandomValue.getInt(-999,-1))
        .build();

    String url = "http://localhost:" + port + "/api/institutes/updateTotalSpots";

    //when
    when(authService.getAccountsInfo()).thenReturn(accounts);


    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<UpdateTotalSpotsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSpotsDto.Response>>(){}
    );


    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());

    // 이후 Dto 내부 오류 메세지 검증 코드 필요
  }


  @Test
  void updateTotalSpots_잘못된_값() {
    //given
    /* 인증 관련 코드 추가 작성 필요*/
    Accounts accounts = fixtureMonkey.giveMeOne(Accounts.class);

    Institutes institutes = accounts.getInstitute();


    String url = "http://localhost:" + port + "/api/institutes/updateTotalSpots";

    //when
    when(authService.getAccountsInfo()).thenReturn(accounts);


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // RequestBody와 Headers를 포함한 HttpEntity 생성
    HttpEntity<Request> entity = new HttpEntity<>(headers);


    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        entity,
        String.class
    );


    ApiResult<UpdateTotalSpotsDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSpotsDto.Response>>(){}
    );


    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());

    // 이후 Dto 내부 오류 메세지 검증 코드 필요
  }

}
