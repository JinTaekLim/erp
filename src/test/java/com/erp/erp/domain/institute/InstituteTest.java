package com.erp.erp.domain.institute;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.institute.common.dto.GetInstituteInfoDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto.Request;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.generator.AccountGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


public class InstituteTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/institute";
  }

  @Autowired
  private TokenManager tokenManager;

  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private AccountRepository accountRepository;

  private Institute createInstitute() {
    Institute institute = InstituteGenerator.get();
    return instituteRepository.save(institute);
  }

  private Account createAccount(Institute institute) {
    Account account = AccountGenerator.get(institute);
    return accountRepository.save(account);
  }


  @Test
  @DisplayName("성공")
  void info() {
    //given
    Institute institute = createInstitute();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/info";

    //when
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Request> request = new HttpEntity<>(headers);


    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        request,
        String.class
    );


    ApiResult<GetInstituteInfoDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<GetInstituteInfoDto.Response>>(){}
    );


    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getTotalSeat()).isEqualTo(institute.getTotalSeat());
    assertThat(apiResponse.getData().getOpenTime()).isEqualTo(institute.getOpenTime());
    assertThat(apiResponse.getData().getCloseTime()).isEqualTo(institute.getCloseTime());
  }

  @Test
  @DisplayName("성공")
  void updateTotalSeat() {
    //given
    Institute institute = createInstitute();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/updateTotalSeat";

    UpdateTotalSeatDto.Request request = fixtureMonkey.giveMeOne(UpdateTotalSeatDto.Request.class);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Request> requestEntity = new HttpEntity<>(request, headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        requestEntity,
        String.class
    );

    ApiResult<UpdateTotalSeatDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateTotalSeatDto.Response>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertThat(apiResponse.getData().getName()).isEqualTo(institute.getName());
    assertThat(apiResponse.getData().getTotalSeat()).isEqualTo(request.getTotalSeat());
  }


  @Test
  @DisplayName("잘못된 Num 값")
  void updateTotalSeat_fail_1() {
    Institute institute = createInstitute();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);
    int totalSeat = RandomValue.getInt(-9999,-1);

    String url = BASE_URL + "/updateTotalSeat";

    UpdateTotalSeatDto.Request request = UpdateTotalSeatDto.Request.builder()
        .totalSeat(totalSeat)
        .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    HttpEntity<Request> requestEntity = new HttpEntity<>(request, headers);

    // when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        requestEntity,
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
  @DisplayName("필수 값 미전달")
  void updateTotalSeat_fail_2() {
    //given
    Institute institute = createInstitute();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/updateTotalSeat";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenDto.getAccessToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Request> requestEntity = new HttpEntity<>(headers);

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        requestEntity,
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
