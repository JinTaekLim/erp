package com.erp.erp.domain.accounts;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.erp.erp.domain.accounts.common.dto.AccountsLoginDto;
import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.accounts.repository.AccountsRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.DisplayName;
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

  @Autowired
  private InstitutesRepository institutesRepository;
  @Autowired
  private AccountsRepository accountsRepository;
  @Autowired
  private TokenManager tokenManager;

  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeOne(Institutes.class);
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Accounts getAccount(Institutes institutes) {
    return fixtureMonkey.giveMeBuilder(Accounts.class)
        .set("institutes", institutes)
        .sample();
  }

  private Accounts createAccount(Institutes institutes) {
    return accountsRepository.save(getAccount(institutes));
  }

  @Test
  void login_성공() {
    //given
    Institutes institutes = createInstitutes();
    Accounts accounts = createAccount(institutes);
    AccountsLoginDto.Request request = AccountsLoginDto.Request.builder()
        .account(accounts.getAccount())
        .password(accounts.getPassword())
        .build();

    String url = "http://localhost:" + port + "/api/accounts/login";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<TokenDto> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<TokenDto>>() {
        }.getType()
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    TokenDto response = apiResponse.getData();
    assertNotNull(response.getAccessToken());
    assertNotNull(response.getRefreshToken());
  }


  @Test
  @DisplayName("필수값 미전달")
  void login_fail_1() {
    //given
    AccountsLoginDto.Request request = AccountsLoginDto.Request.builder().build();

    String url = "http://localhost:" + port + "/api/accounts/login";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<TokenDto> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<TokenDto>>() {
        }.getType()
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNotNull(apiResponse);
    assertNull(apiResponse.getData());
  }

  @Test
  @DisplayName("존재하지 않거나 잘못된 아이디 혹은 비밀번호")
  void login_fail_2() {
    //given
    AccountsLoginDto.Request request = fixtureMonkey.giveMeOne(AccountsLoginDto.Request.class);

    String url = "http://localhost:" + port + "/api/accounts/login";

    //when
    ResponseEntity<String> responseEntity;
    try {
      responseEntity = restTemplate.postForEntity(url, request, String.class);
    } catch (Exception e) {
      responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void reissueToken() {
    //given
    Institutes institutes = createInstitutes();
    Accounts accounts = createAccount(institutes);
    TokenDto tokenDto = tokenManager.createToken(accounts);

    String url = "http://localhost:" + port + "/api/accounts/reissueToken?refreshToken=" + tokenDto.getRefreshToken();


    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        null,
        String.class
    );

    ApiResult<TokenDto> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<TokenDto>>() {
        }.getType()
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);

    TokenDto response = apiResponse.getData();
    assertNotNull(response.getAccessToken());
    assertNotNull(response.getRefreshToken());
  }

}
