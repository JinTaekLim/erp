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
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PlanControllerTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/plan";
  }

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private PlanRepository planRepository;

  private Plan createPlan() {
    return planRepository.save(PlanGenerator.get());
  }

  @Test
  @DisplayName("getPlans 성공")
  void getPlans() {
    // given
    LicenseType licenseType = RandomValue.getRandomEnum(LicenseType.class);
    int planCount = RandomValue.getInt(0,5);

    List<Plan> plans = IntStream.range(0, planCount)
        .mapToObj(i -> createPlan())
        .filter(plan -> plan.getLicenseType() == licenseType)
        .toList();


    String url = BASE_URL + "/getPlans/" + licenseType;

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
    assertThat(apiResponse.getData().size()).isEqualTo(plans.size());
    IntStream.range(0, plans.size()).forEach(i -> {
      assertThat(apiResponse.getData().get(i).getId()).isEqualTo(plans.get(i).getId());
      assertThat(apiResponse.getData().get(i).getPlanType()).isEqualTo(plans.get(i).getPlanType());
      assertThat(apiResponse.getData().get(i).getLicenseType()).isEqualTo(plans.get(i).getLicenseType());
      assertThat(apiResponse.getData().get(i).getCourseType()).isEqualTo(plans.get(i).getCourseType());
      assertThat(apiResponse.getData().get(i).getName()).isEqualTo(plans.get(i).getName());
      assertThat(apiResponse.getData().get(i).getPrice()).isEqualTo(plans.get(i).getPrice());
    });
  }

  @Test
  @DisplayName("요청 잘못된 값 전달")
  void getPlans_fail() {
    // given
    int randomInt = RandomValue.getInt();
    String url = BASE_URL + "/getPlans/" + randomInt;

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