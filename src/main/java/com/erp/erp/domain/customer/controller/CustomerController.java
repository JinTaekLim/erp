package com.erp.erp.domain.customer.controller;

import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.service.CustomerService;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "customer" ,description = "고객 관리")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;

  @Operation(summary = "고객 추가")
  @PostMapping(value = "/addCustomer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResult<AddCustomerDto.Response> addCustomer(
      @Valid @RequestPart AddCustomerDto.Request req,
      @RequestPart(value = "file", required = false) MultipartFile file
      ) {
    AddCustomerDto.Response response = customerService.addCustomer(req, file);
    return ApiResult.success(response);
  }

  @Operation(summary = "고객 정보 수정")
  @PutMapping(value = "/updateCustomer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResult<UpdateCustomerDto.Response> updatedCustomerInfo(
      @Valid @RequestPart UpdateCustomerDto.Request req,
      @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    UpdateCustomerDto.Response response = customerService.updateCustomer(req, file);
    return ApiResult.success(response);
  }

  @Operation(summary = "고객 상태 값 변경")
  @PutMapping("/updateStatus")
  public ApiResult<CustomerStatus> updateStatus(@RequestBody @Valid UpdateStatusDto.Request req) {
    CustomerStatus status = customerService.updateStatus(req);
    return ApiResult.success(status);
  }

  @Operation(summary = "이용 중 고객 조회")
  @GetMapping("currentCustomers")
  public ApiResult<List<GetCustomerDto.Response>> getCurrentCustomers(
      @RequestBody @Valid GetCustomerDto.Request req
  ) {
    List<GetCustomerDto.Response> response = customerService.getCurrentCustomers(req);
    return ApiResult.success(response);
  }


  @Operation(summary = "만료된 고객 조회")
  @GetMapping("expiredCustomer/{page}")
  public ApiResult<List<GetCustomerDto.Response>> getExpiredCustomers(@PathVariable("page") int page) {
    List<GetCustomerDto.Response> response = customerService.getExpiredCustomers(page);
    return ApiResult.success(response);
  }

  @Operation(summary = "이용 가능 모든 고객 이름 조회")
  @GetMapping("/getAvailableCustomerNames")
  public ApiResult<List<GetAvailableCustomerNamesDto.Response>> getAvailableCustomerNames() {
    List<GetAvailableCustomerNamesDto.Response> response = customerService.getCurrentCustomers();
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

  @Operation(summary = "고객 검색 ( 글자 기준, 초성X )")
  @GetMapping("/searchCustomer/{customerName}")
  public ApiResult<List<GetCustomerDto.Response>> searchCustomer(
      @PathVariable("customerName") String customerName
  ) {
    List<GetCustomerDto.Response> response = customerService.searchCustomer(customerName);
    return ApiResult.success(response);
  }

}
