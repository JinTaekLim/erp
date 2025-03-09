package com.erp.erp.domain.plan;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.auth.business.TokenManager;
import com.erp.erp.domain.auth.common.dto.TokenDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.dto.GetPlanDto;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.HttpEntityUtil;
import com.erp.erp.global.util.generator.AccountGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PlanTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/plan";
  }

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private InstituteRepository instituteRepository;

  @Autowired
  private PlanRepository planRepository;

  @Autowired
  private TokenManager tokenManager;

  private Plan createPlan() {
    return planRepository.save(PlanGenerator.get());
  }

  private Account createAccount(Institute institute) {
    return accountRepository.save(AccountGenerator.get(institute));
  }

  private Institute createInstitutes() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  @Test
  @DisplayName("getPlans 성공")
  void getPlans() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    LicenseType licenseType = RandomValue.getRandomEnum(LicenseType.class);
    int planCount = RandomValue.getInt(0,5);

    List<Plan> plans = IntStream.range(0, planCount)
        .mapToObj(i -> createPlan())
        .filter(plan -> plan.getLicenseType() == licenseType)
        .toList();


    String url = BASE_URL + "/getPlans/" + licenseType;

    HttpEntity<Void> httpRequest = HttpEntityUtil.setToken(null, tokenDto.getAccessToken());

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        httpRequest,
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
      assertThat(apiResponse.getData().get(i).getPlanName()).isEqualTo(plans.get(i).getName());
      assertThat(apiResponse.getData().get(i).getPrice()).isEqualTo(plans.get(i).getPrice());
    });
  }

  @Test
  @DisplayName("요청 잘못된 값 전달")
  void getPlans_fail() {
    // given
    Institute institute = createInstitutes();
    Account account = createAccount(institute);
    TokenDto tokenDto = tokenManager.createToken(account);

    int randomInt = RandomValue.getInt();
    String url = BASE_URL + "/getPlans/" + randomInt;

    HttpEntity<Void> httpRequest = HttpEntityUtil.setToken(null, tokenDto.getAccessToken());

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.GET,
        httpRequest,
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