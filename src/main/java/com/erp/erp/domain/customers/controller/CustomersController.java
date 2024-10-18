package com.erp.erp.domain.customers.controller;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.service.AuthService;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto.Response;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.service.CustomersService;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.global.error.ApiResult;
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
  @PostMapping("/addCustomer")
  public ApiResult<AddCustomerDto.Response> addCustomer(
      @RequestBody AddCustomerDto.Request req
      ) {
    Accounts accounts = authService.getAccountsInfo();
    Institutes institutes = accounts.getInstitute();


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

  @PostMapping("/updateStatus")
  public ApiResult<Boolean> updateStatus(@RequestBody UpdateStatusDto.Request req) {
    boolean status = customersService.updateStatus(req);
    return ApiResult.success(status);
  }

  @GetMapping("customers/{page}")
  public ApiResult<?> getCurrentCustomers(@PathVariable Long page) {
    return ApiResult.success(null);
  }

  @GetMapping("expiredCustomer/{page}")
  public ApiResult<?> getExpiredCustomers(@PathVariable Long page) {
    return ApiResult.success(null);
  }
}
