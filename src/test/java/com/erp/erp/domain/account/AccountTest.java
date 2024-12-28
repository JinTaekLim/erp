package com.erp.erp.domain.account;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.erp.erp.domain.account.common.dto.AccountLoginDto;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.auth.common.exception.InvalidTokenException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class AccountTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/account";
  }

  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private TokenManager tokenManager;

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Account createAccount(Institute institute) {
    return accountRepository.save(AccountGenerator.get(institute));
  }

  @Test
  @DisplayName("성공")
  void login() {
    //given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    AccountLoginDto.Request request = AccountLoginDto.Request.builder()
        .account(account.getAccountId())
        .password(account.getPassword())
        .build();

    String url = BASE_URL + "/login";

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
    AccountLoginDto.Request request = AccountLoginDto.Request.builder().build();

    String url = BASE_URL + "/login";

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
    AccountLoginDto.Request request = fixtureMonkey.giveMeOne(AccountLoginDto.Request.class);

    String url = BASE_URL + "/login";

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
  @DisplayName("성공")
  void reissueToken() {
    //given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    String url = BASE_URL + "/reissueToken?refreshToken=" + tokenDto.getRefreshToken();


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
