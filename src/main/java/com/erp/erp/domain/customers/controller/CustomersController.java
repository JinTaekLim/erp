package com.erp.erp.domain.customers.controller;

import com.erp.erp.domain.customers.common.dto.AddCustomerDto;
import com.erp.erp.domain.customers.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customers.common.dto.GetCustomerDto;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.service.CustomersService;
import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.plans.service.PlansService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
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
  private final PlansService plansService;

  // note. 이후 프로필 사진 관련 처리 필요
  // 로직 책임 분리 필요
  @Operation(summary = "고객 추가")
  @PostMapping("/addCustomer")
  public ApiResult<AddCustomerDto.Response> addCustomer(
      @Valid @RequestBody AddCustomerDto.Request req
      ) {
    Payments payments = customersService.addCustomer(req);
    Customers customers = payments.getCustomers();

    AddCustomerDto.Response response = AddCustomerDto.Response.fromEntity(payments,customers);

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

    List<Customers> customersList = customersService.getCurrentCustomers(page);

    List<GetCustomerDto.Response> response = customersList.stream()
        .map(GetCustomerDto.Response::fromEntity)
        .toList();

    return ApiResult.success(response);
  }


  @Operation(summary = "만료된 고객 조회")
  @GetMapping("expiredCustomer/{page}")
  public ApiResult<List<GetCustomerDto.Response>> getExpiredCustomers(@PathVariable int page) {
    List<Customers> customersList = customersService.getExpiredCustomers(page);

    List<GetCustomerDto.Response> response = customersList.stream()
        .map(GetCustomerDto.Response::fromEntity)
        .toList();


    return ApiResult.success(response);
  }

  @Operation(summary = "이용 가능 모든 고객 이름 조회")
  @GetMapping("getAvailableCustomerNames")
  public ApiResult<List<GetAvailableCustomerNamesDto.Response>> getAvailableCustomerNames() {
    List<Customers> customersList = customersService.getCurrentCustomers();

    List<GetAvailableCustomerNamesDto.Response> response = customersList.stream()
        .map(GetAvailableCustomerNamesDto.Response::fromEntity)
        .toList();

    return ApiResult.success(response);
  }

}
