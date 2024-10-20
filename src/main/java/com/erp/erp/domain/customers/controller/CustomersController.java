package com.erp.erp.domain.customers.controller;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.service.AuthService;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto.Response;
import com.erp.erp.domain.customers.common.dto.GetCustomerDto;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.service.CustomersService;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomersController {

  private final CustomersService customersService;
  private final AuthService authService;

  // note. 이후 프로필 사진 관련 처리 필요
  @Operation(summary = "고객 추가")
  @PostMapping("/addCustomer")
  public ApiResult<AddCustomerDto.Response> addCustomer(
      @Valid @RequestBody AddCustomerDto.Request req
      ) {
    Accounts accounts = authService.getAccountsInfo();
    Institutes institutes = accounts.getInstitutes();


    Customers customers = customersService.addCustomer(institutes,req);

    AddCustomerDto.Response response = Response.builder()
        .name(customers.getName())
        .gender(customers.getGender())
        .phone(customers.getPhone())
        .address(customers.getAddress())
        .birthDate(customers.getBirthDate())
        .photoUrl(customers.getPhotoUrl())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "고객 상태 값 변경")
  @PostMapping("/updateStatus")
  public ApiResult<Boolean> updateStatus(@RequestBody UpdateStatusDto.Request req) {
    boolean status = customersService.updateStatus(req);
    return ApiResult.success(status);
  }

  @Operation(summary = "이용 중 고객 조회")
  @GetMapping("currentCustomers/{page}")
  public ApiResult<List<GetCustomerDto.Response>> getCurrentCustomers(@PathVariable int page) {
    Accounts accounts = authService.getAccountsInfo();
    Long institutesId = accounts.getInstitutes().getId();

    List<Customers> customersList = customersService.getCurrentCustomers(institutesId, page);

    List<GetCustomerDto.Response> response = customersList.stream()
        .map(customers -> GetCustomerDto.Response.builder()
            .photoUrl(customers.getPhotoUrl())
            .name(customers.getName())
            .gender(String.valueOf(customers.getGender()))
            .phone(customers.getPhone())
            .plans(customers.getInstitutes().getName())
            .remainingTime(0)
            .usedTime(0)
            .registrationDate(0)
            .tardinessCount(0)
            .absenceCount(0)
            .build())
        .collect(Collectors.toList());

    return ApiResult.success(response);
  }


  @Operation(summary = "만료된 고객 조회")
  @GetMapping("expiredCustomer/{page}")
  public ApiResult<?> getExpiredCustomers(@PathVariable int page) {
    return ApiResult.success(null);
  }
}
