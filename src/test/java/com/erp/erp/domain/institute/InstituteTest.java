package com.erp.erp.domain.institute;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto.Request;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


public class InstituteTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/institute";
  }


  @MockBean
  private AuthProvider authProvider;

  @Test
  void updateTotalSpots_성공() {
    //given
    /* 인증 관련 코드 추가 작성 필요*/
    Account account = fixtureMonkey.giveMeOne(Account.class);
    
    Institute institute = account.getInstitute();

    UpdateTotalSeatDto.Request request = fixtureMonkey.giveMeOne(
        UpdateTotalSeatDto.Request.class
    );;

    String url = BASE_URL + "/updateTotalSeat";

    //when
    when(authProvider.getCurrentInstitute()).thenReturn(institute);


    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<UpdateTotalSeatDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSeatDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    UpdateTotalSeatDto.Response response = apiResponse.getData();
    assertThat(institute.getId()).isEqualTo(response.getId());
    assertThat(institute.getName()).isEqualTo(response.getName());
    assertThat(institute.getTotalSeat()).isEqualTo(response.getNum());
  }


  @Test
  void updateTotalSpots_잘못된_Num값() {
    //given
    /* 인증 관련 코드 추가 작성 필요*/
    Account account = fixtureMonkey.giveMeOne(Account.class);

    Institute institute = account.getInstitute();

    UpdateTotalSeatDto.Request request = UpdateTotalSeatDto.Request.builder()
        .num(RandomValue.getInt(-999,-1))
        .build();

    String url = BASE_URL + "/updateTotalSeat";

    //when
    when(authProvider.getCurrentInstitute()).thenReturn(institute);

    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<UpdateTotalSeatDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSeatDto.Response>>(){}
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
    Account account = fixtureMonkey.giveMeOne(Account.class);

    Institute institute = account.getInstitute();


    String url = BASE_URL + "/updateTotalSeat";

    //when
    when(authProvider.getCurrentInstitute()).thenReturn(institute);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // RequestBody와 Headers를 포함한 HttpEntity 생성
    HttpEntity<Request> entity = new HttpEntity<>(headers);


    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        entity,
        String.class
    );


    ApiResult<UpdateTotalSeatDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSeatDto.Response>>(){}
    );


    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());

    // 이후 Dto 내부 오류 메세지 검증 코드 필요
  }

}
