package com.erp.erp.domain.plan.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PlanControllerTest extends IntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private PlanRepository planRepository;

  private Plan createPlan() {
    return planRepository.save(PlanGenerator.get());
  }

  private List<Plan> createPlans(int size) {
    return planRepository.saveAll(PlanGenerator.planList(size));
  }

  @Test
  @DisplayName("동일한 구분 값 반환 여부 확인")
  void getPlans_success_1() {
    // given
    int size = RandomValue.getInt(0,5);
    createPlans(size);

    LicenseType licenseType = RandomValue.getRandomEnum(LicenseType.class);

    String url = "http://localhost:" + port + "/api/plan/getPlans/" + licenseType;

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );


    ApiResult<List<GetPlanDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetPlanDto.Response>>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    apiResponse.getData().forEach(response ->
        assertThat(response.getLicenseType()).isEqualTo(licenseType)
    );
  }

  @Test
  @DisplayName("내부 값 비교")
  void getPlans_success_2() {
    // given
    int size = RandomValue.getInt(0,5);
    LicenseType licenseType = RandomValue.getRandomEnum(LicenseType.class);
    List<Plan> plans = planRepository.saveAll(PlanGenerator.planList(size,licenseType));


    String url = "http://localhost:" + port + "/api/plan/getPlans/" + licenseType;

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );


    ApiResult<List<GetPlanDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetPlanDto.Response>>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    assertThat(apiResponse.getData()).hasSameClassAs(plans);

    apiResponse.getData().sort(Comparator.comparing(GetPlanDto.Response::getId).reversed());
    plans.sort(Comparator.comparing(Plan::getId).reversed());


    IntStream.range(0,size).forEach(i -> {
      GetPlanDto.Response actualList = apiResponse.getData().get(i);
      Plan expectedList = plans.get(i);
      assertThat(actualList.getPlanType()).isEqualTo(expectedList.getPlanType());
      assertThat(actualList.getLicenseType()).isEqualTo(expectedList.getLicenseType());
      assertThat(actualList.getId()).isEqualTo(expectedList.getId());
      assertThat(actualList.getName()).isEqualTo(expectedList.getName());
      assertThat(actualList.getPrice()).isEqualTo(expectedList.getPrice());
    });
  }

  @Test
  @DisplayName("요청 잘못된 값 전달")
  void getPlans_fail() {
    // given
    int randomInt = RandomValue.getInt();
    String url = "http://localhost:" + port + "/api/plan/getPlans/" + randomInt;

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );


    ApiResult<List<GetPlanDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetPlanDto.Response>>>(){}
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());

  }
}