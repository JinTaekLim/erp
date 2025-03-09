package com.erp.erp.domain.admin;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.account.common.exception.NotFoundAccountException;
import com.erp.erp.domain.account.repository.AccountRepository;
import com.erp.erp.domain.admin.business.HttpSessionManager;
import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.admin.common.dto.GetAccountDto;
import com.erp.erp.domain.admin.common.dto.LoginDto;
import com.erp.erp.domain.admin.common.dto.UpdateAccountDto;
import com.erp.erp.domain.admin.common.entity.Admin;
import com.erp.erp.domain.admin.common.exception.NotFoundAdminException;
import com.erp.erp.domain.admin.common.exception.UnauthorizedAccessException;
import com.erp.erp.domain.admin.repository.AdminRepository;
import com.erp.erp.domain.admin.common.dto.GetInstituteDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.exception.NotFoundInstituteException;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.global.response.ApiResult;
import com.erp.erp.global.util.ReflectionUtil;
import com.erp.erp.global.util.generator.AccountGenerator;
import com.erp.erp.global.util.generator.AdminGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class adminTest extends IntegrationTest {

  private String BASE_URL;

  @BeforeEach
  void setUp() {
    BASE_URL = "http://localhost:" + port + "/api/admin";
  }

  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private AdminRepository adminRepository;
  @MockBean
  private HttpSessionManager httpSessionManager;


  private Institute createInstitute() {
    return instituteRepository.save(InstituteGenerator.get());
  }
  private Account createAccount(Institute institute) {
    return accountRepository.save(AccountGenerator.get(institute));
  }
  private Admin createAdmin() {
    return adminRepository.save(AdminGenerator.get());
  }

  /*
  note. 임시 코드
  테스트 환경의 RequestContextHolder.currentRequestAttributes() 를 임의로 설정할 수 있도록 설정 필요
   */
  private void setSession(Admin admin) {
    HttpSession session = new MockHttpSession();
    session.setAttribute("adminId", admin.getId());
    when(httpSessionManager.getSession()).thenReturn(session);
    when(httpSessionManager.getValue(any(), any())).thenReturn(admin.getId());
  }

  @Test
  @DisplayName("addPlans 성공")
  void addPlans() {
    //given
    Admin admin = createAdmin();
    setSession(admin);

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
    assertEquals(apiResponse.getData().getCourseType(), request.getCourseType());
    assertEquals(apiResponse.getData().getPrice(), request.getPrice());
    assertEquals(apiResponse.getData().getPlanName(), request.getPlanName());
  }

  @Test
  @DisplayName("필수값 미입력")
  void addPlans_fail_1() {
    //given
    AddPlanDto.Request request = AddPlanDto.Request.builder().build();

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
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNotNull(apiResponse);
  }

  @Test
  @DisplayName("성공")
  void addInstitute() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

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
    assertThat(apiResponse.getData().getTotalSeat()).isEqualTo(req.getTotalSeat());
    assertThat(apiResponse.getData().getOpenTime()).isEqualTo(req.getOpenTime());
    assertThat(apiResponse.getData().getCloseTime()).isEqualTo(req.getCloseTime());
  }

  @Test
  @DisplayName("필수값 미입력")
  void addInstitute_fail() {
    // given
    AddInstituteDto.Request req = AddInstituteDto.Request.builder().build();

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
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertNull(apiResponse.getData());
  }


  @Test
  @DisplayName("getAccounts 성공")
  void getAccounts() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    Institute otherInstitute = createInstitute();
    int otherAccountSize = RandomValue.getInt(0,5);
    IntStream.range(0, otherAccountSize)
        .mapToObj(i -> {
          return createAccount(otherInstitute);}).toList();

    Institute institute = createInstitute();
    int accountSize = RandomValue.getInt(0,5);
    List<Account> accounts = IntStream.range(0, accountSize)
        .mapToObj(i -> {
          return createAccount(institute);
        }).toList();

    String url = BASE_URL + "/getAccounts?instituteId=" + institute.getId();

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetAccountDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetAccountDto.Response>>>() {
        }.getType()
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(apiResponse.getData());
    IntStream.range(0, accountSize).forEach(i -> {
      GetAccountDto.Response response = apiResponse.getData().get(i);
      Account account = accounts.get(i);

      assertThat(response.getAccountId()).isEqualTo(account.getId());
      assertThat(response.getIdentifier()).isEqualTo(account.getIdentifier());
      assertThat(response.getName()).isEqualTo(account.getName());
    });
  }

  @Test
  @DisplayName("성공")
  void addAccount() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    Institute institute = createInstitute();
    AddAccountDto.Request req = fixtureMonkey.giveMeBuilder(AddAccountDto.Request.class)
        .set("instituteId", institute.getId())
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
    assertThat(apiResponse.getData().getInstituteId()).isEqualTo(req.getInstituteId());
    assertThat(apiResponse.getData().getIdentifier()).isEqualTo(req.getIdentifier());
    assertThat(apiResponse.getData().getCreatedId()).isEqualTo(String.valueOf(admin.getId()));
    assertThat(apiResponse.getData().getName()).isEqualTo(req.getName());

  }

  @Test
  @DisplayName("addAccount 세션 미보유")
  void addAccount_fail_1() {
    // given
    Institute institute = createInstitute();
    AddAccountDto.Request req = fixtureMonkey.giveMeBuilder(AddAccountDto.Request.class)
        .set("instituteId", institute.getId())
        .sample();

    String url = BASE_URL + "/addAccount";

    UnauthorizedAccessException exception = new UnauthorizedAccessException();

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
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
    assertThat(apiResponse.getData()).isNull();
  }

  @Test
  @DisplayName("존재하지 않는 매장 값 전달")
  void addAccount_fail_2() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

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

  @Test
  @DisplayName("존재하는 아이디 값 전달")
  void addAccount_fail_3() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    Institute institute = createInstitute();
    Account account = createAccount(institute);

    AddAccountDto.Request req = AddAccountDto.Request.builder()
        .identifier(account.getIdentifier())
        .password(account.getPassword())
        .instituteId(institute.getId())
        .build();


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
  }

  @Test
  @DisplayName("존재하지 않는 관리자 요청")
  void addAccount_fail_4() {
    // given
    Admin admin = fixtureMonkey.giveMeBuilder(Admin.class).sample();
    ReflectionUtil.setFieldValue(admin, "id", RandomValue.getRandomLong(0, 9999));
    setSession(admin);

    Institute institute = createInstitute();

    AddAccountDto.Request req = fixtureMonkey.giveMeBuilder(AddAccountDto.Request.class)
        .set("instituteId", institute.getId())
        .sample();

    NotFoundAdminException exception = new NotFoundAdminException();

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
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
  }

  @Test
  void getInstitute() {
    // given
    int instituteCount = RandomValue.getInt(1,5);
    List<Institute> institutes = IntStream.range(0,instituteCount).mapToObj(
        i-> {return createInstitute();}
    ).toList();

    String url = BASE_URL + "/getInstitutes";

    // when
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
        url,
        String.class
    );

    ApiResult<List<GetInstituteDto.Response>> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<List<GetInstituteDto.Response>>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(instituteCount).isEqualTo(apiResponse.getData().size());
    IntStream.range(0,instituteCount).forEach(
        i-> {
          assertThat(apiResponse.getData().get(i).getId()).isEqualTo(institutes.get(i).getId());
          assertThat(apiResponse.getData().get(i).getName()).isEqualTo(institutes.get(i).getName());
          assertThat(apiResponse.getData().get(i).getOpenTime()).isEqualTo(institutes.get(i).getOpenTime());
          assertThat(apiResponse.getData().get(i).getCloseTime()).isEqualTo(institutes.get(i).getCloseTime());
        }
    );
  }

  @Test
  void updateAccount() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    Institute institute = createInstitute();
    Account account = createAccount(institute);

    UpdateAccountDto.Request req = fixtureMonkey.giveMeBuilder(UpdateAccountDto.Request.class)
        .set("accountId", account.getId())
        .sample();

    String url = BASE_URL + "/updateAccount";

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PATCH,
        new HttpEntity<>(req),
        String.class
    );

    ApiResult<UpdateAccountDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateAccountDto.Response>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(apiResponse.getData().getAccountId()).isEqualTo(account.getId());
    assertThat(apiResponse.getData().getIdentifier()).isEqualTo(req.getIdentifier());
  }

  @Test
  @DisplayName("잘못된 accountId")
  void updateAccount_fail() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    UpdateAccountDto.Request req = fixtureMonkey.giveMeBuilder(UpdateAccountDto.Request.class)
        .set("accountId", RandomValue.getRandomLong(0,9999))
        .sample();

    String url = BASE_URL + "/updateAccount";

    NotFoundAccountException exception = new NotFoundAccountException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.PATCH,
        new HttpEntity<>(req),
        String.class
    );

    ApiResult<UpdateAccountDto.Response> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<UpdateAccountDto.Response>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
  }

  @Test
  void lockAccount() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    Institute institute = createInstitute();
    Account account = createAccount(institute);

    String url = BASE_URL + "/deleteAccount";

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        new HttpEntity<>(account.getId()),
        String.class
    );

    ApiResult<?> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<?>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void login() {
    // given
    Admin admin = createAdmin();
    setSession(admin);

    LoginDto.Request req = LoginDto.Request.builder()
        .identifier(admin.getIdentifier())
        .password(admin.getPassword())
        .build();

    String url = BASE_URL + "/login";

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.POST,
        new HttpEntity<>(req),
        String.class
    );

    ApiResult<Boolean> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Boolean>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("잘못된 아이디/비밀번호 입력")
  void login_fail_1() {
    // given
    Admin admin = createAdmin();

    LoginDto.Request req = LoginDto.Request.builder()
        .identifier(admin.getIdentifier() + RandomValue.string(5,10).setNullable(false))
        .password(admin.getPassword() + RandomValue.string(5,10).setNullable(false))
        .build();

    String url = BASE_URL + "/login";

    NotFoundAdminException exception = new NotFoundAdminException();

    // when
    ResponseEntity<String> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.POST,
        new HttpEntity<>(req),
        String.class
    );

    ApiResult<Boolean> apiResponse = gson.fromJson(
        responseEntity.getBody(),
        new TypeToken<ApiResult<Boolean>>() {
        }
    );

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(apiResponse.getMessage()).isEqualTo(exception.getMessage());
    assertThat(apiResponse.getCode()).isEqualTo(exception.getCode());
  }
}