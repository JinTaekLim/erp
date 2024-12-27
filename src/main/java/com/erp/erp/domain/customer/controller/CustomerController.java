package com.erp.erp.domain.customer.controller;

import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.service.CustomerService;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/customer")
@Tag(name = "customer" ,description = "고객 관리")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;

  // note. 이후 프로필 사진 관련 처리 필요
  // 로직 책임 분리 필요
  @Operation(summary = "고객 추가")
  @PostMapping("/addCustomer")
  public ApiResult<AddCustomerDto.Response> addCustomer(
      @Valid @RequestBody AddCustomerDto.Request req
      ) {
    AddCustomerDto.Response response = customerService.addCustomer(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "고객 정보 수정")
  @PostMapping("/updateCustomer")
  public ApiResult<UpdateCustomerDto.Response> updatedCustomerInfo(
      @Valid @RequestBody UpdateCustomerDto.Request req
  ) {
    UpdateCustomerDto.Response response = customerService.updateCustomer(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "고객 상태 값 변경")
  @PostMapping("/updateStatus")
  public ApiResult<CustomerStatus> updateStatus(@RequestBody UpdateStatusDto.Request req) {
    CustomerStatus status = customerService.updateStatus(req);
    return ApiResult.success(status);
  }

  @Operation(summary = "이용 중 고객 조회")
  @GetMapping("currentCustomers/{page}")
  public ApiResult<List<GetCustomerDto.Response>> getCurrentCustomers(@PathVariable("page") int page) {

    List<Customer> customerList = customerService.getCurrentCustomers(page);

    List<GetCustomerDto.Response> response = customerList.stream()
        .map(GetCustomerDto.Response::fromEntity)
        .toList();

    return ApiResult.success(response);
  }


  @Operation(summary = "만료된 고객 조회")
  @GetMapping("expiredCustomer/{page}")
  public ApiResult<List<GetCustomerDto.Response>> getExpiredCustomers(@PathVariable("page") int page) {
    List<Customer> customerList = customerService.getExpiredCustomers(page);

    List<GetCustomerDto.Response> response = customerList.stream()
        .map(GetCustomerDto.Response::fromEntity)
        .toList();


    return ApiResult.success(response);
  }

  @Operation(summary = "이용 가능 모든 고객 이름 조회")
  @GetMapping("/getAvailableCustomerNames")
  public ApiResult<List<GetAvailableCustomerNamesDto.Response>> getAvailableCustomerNames() {
    List<Customer> customerList = customerService.getCurrentCustomers();

    List<GetAvailableCustomerNamesDto.Response> response = customerList.stream()
        .map(GetAvailableCustomerNamesDto.Response::fromEntity)
        .toList();

    return ApiResult.success(response);
  }

  @Operation(summary = "고객 이름 검색 ( 글자 기준, 초성X )")
  @GetMapping("/searchCustomerName/{keyword}")
  public ApiResult<List<SearchCustomerNameDto.Response>> searchCustomerName(
      @PathVariable("keyword") String keyword
  ) {
    List<SearchCustomerNameDto.Response> response = customerService.searchCustomerName(keyword);
    return ApiResult.success(response);
  }

  @Operation(summary = "고객 상세 조회")
  @GetMapping("/getCustomerDetail/{customerId}")
  public ApiResult<GetCustomerDetailDto.Response> getCustomerDetail(@PathVariable("customerId") Long customerId) {
    GetCustomerDetailDto.Response response = customerService.getCustomerDetail(customerId);
    return ApiResult.success(response);
  }
}
