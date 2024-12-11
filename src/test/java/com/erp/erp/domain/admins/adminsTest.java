package com.erp.erp.domain.admins;

import com.erp.erp.domain.admins.common.dto.AddAccountDto;
import com.erp.erp.domain.admins.common.dto.AddInstituteDto;
import com.erp.erp.domain.admins.common.dto.AddPlanDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.common.exception.NotFoundInstituteException;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
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

class adminsTest extends IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private InstitutesRepository institutesRepository;

  private Institutes getInstitutes() {
      return fixtureMonkey.giveMeOne(Institutes.class);
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

    String url = "http://localhost:" + port + "/api/admin/addPlans";

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

    String url = "http://localhost:" + port + "/api/admin/addInstitute";

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

        String url = "http://localhost:" + port + "/api/admin/addAccount";

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


    @Test()
    void addAccount_fail() {
        // given
        AddAccountDto.Request req = fixtureMonkey.giveMeOne(AddAccountDto.Request.class);

        NotFoundInstituteException exception = new NotFoundInstituteException();
        String url = "http://localhost:" + port + "/api/admin/addAccount";

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