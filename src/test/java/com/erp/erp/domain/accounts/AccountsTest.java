package com.erp.erp.domain.accounts;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto;
import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto.Request;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class AccountsTest extends IntegrationTest {


  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void login_성공() {
    //given
    AccountsLoginDto.Request request = fixtureMonkey.giveMeOne(
        AccountsLoginDto.Request.class
    );

    TokenDto tokenDto = fixtureMonkey.giveMeOne(TokenDto.class);

    String url = "http://localhost:" + port + "/api/accounts/login";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<TokenDto> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<TokenDto>>(){}
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    TokenDto response = apiResponse.getData();
    assertNotNull(response.getAccessToken());
    assertNotNull(response.getRefreshToken());
  }


  @Test
  void login_실패() {
    //given
    AccountsLoginDto.Request request = new Request();

    TokenDto tokenDto = fixtureMonkey.giveMeOne(TokenDto.class);

    String url = "http://localhost:" + port + "/api/accounts/login";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );


    ApiResult<TokenDto> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        ApiResult.class
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNotNull(apiResponse);
    assertNull(apiResponse.getData());
  }

}
