package com.erp.erp.domain.admin;

import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.common.exception.NotFoundInstituteException;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class adminTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/admin";
  }

  @Autowired
  private InstitutesRepository institutesRepository;

  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Institutes createInstitute() {
    return institutesRepository.saveAndFlush(getInstitutes());
  }


  @Test
  void addPlans() {
    //given
    AddPlanDto.Request request = fixtureMonkey.giveMeBuilder(AddPlanDto.Request.class)
        .set("licenseType", RandomValue.getRandomEnum(LicenseType.class))
        .sample();

    String url = BASE_URL + "/addPlan";

    //when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        request,
        String.class
    );

    ApiResult<AddPlanDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddPlanDto.Response>>() {
        }
    );

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse);
    assertEquals(apiResponse.getData().getLicenseType(), request.getLicenseType());
    assertEquals(apiResponse.getData().getPrice(), request.getPrice());
    assertEquals(apiResponse.getData().getName(), request.getName());
  }

  @Test()
  void addInstitute() {
    // given
    AddInstituteDto.Request req = fixtureMonkey.giveMeOne(AddInstituteDto.Request.class);

    String url = BASE_URL + "/addInstitute";

    // when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        req,
        String.class
    );

    ApiResult<AddInstituteDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddInstituteDto.Response>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData().getName()).isEqualTo(req.getName());
    assertThat(apiResponse.getData().getTotalSpots()).isEqualTo(req.getTotalSpots());
  }


  @Test()
  void addAccount() {
    // given
    Institutes institutes = createInstitute();
    AddAccountDto.Request req = fixtureMonkey.giveMeBuilder(AddAccountDto.Request.class)
        .set("instituteId", institutes.getId())
        .sample();

    String url = BASE_URL + "/addAccount";

    // when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        req,
        String.class
    );

    ApiResult<AddAccountDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddAccountDto.Response>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
//        assertThat(apiResponse.getData().getName()).isEqualTo(req.getName());
//        assertThat(apiResponse.getData().getTotalSpots()).isEqualTo(req.getTotalSpots());
  }


  @Test
  void addAccount_fail() {
    // given
    long randomLong = RandomValue.getRandomLong(0, 9999);
    AddAccountDto.Request req = fixtureMonkey.giveMeBuilder(AddAccountDto.Request.class)
        .set("instituteId", randomLong)
        .sample();

    NotFoundInstituteException exception = new NotFoundInstituteException();
    String url = BASE_URL + "/addAccount";

    // when
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(
        url,
        req,
        String.class
    );

    ApiResult<AddAccountDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<AddAccountDto.Response>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }


}