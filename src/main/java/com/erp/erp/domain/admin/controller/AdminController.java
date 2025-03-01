package com.erp.erp.domain.admin.controller;


import com.erp.erp.domain.admin.common.dto.AddAccountDto;
import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.admin.common.dto.AddPlanDto;
import com.erp.erp.domain.admin.common.dto.LoginDto;
import com.erp.erp.domain.admin.common.dto.UpdateAccountDto;
import com.erp.erp.domain.admin.service.AdminService;
import com.erp.erp.domain.admin.common.dto.GetInstituteDto;
import com.erp.erp.global.annotation.authentication.Admin;
import com.erp.erp.global.annotation.authentication.PermitAll;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "관리자용")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

  private final AdminService adminService;

  @Operation(summary = "이용권 등록")
  @PostMapping("/addPlan")
  @Admin
  public ApiResult<AddPlanDto.Response> addPlans(
      @RequestBody @Valid AddPlanDto.Request request
  ) {
    AddPlanDto.Response response = adminService.addPlans(request);
    return ApiResult.success(response);
  }

  @Operation(summary = "매장 등록")
  @PostMapping("/addInstitute")
  @Admin
  public ApiResult<AddInstituteDto.Response> addInstitute(
      @RequestBody @Valid AddInstituteDto.Request req
  ) {
    AddInstituteDto.Response response = adminService.addInstitute(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "계정 추가")
  @PostMapping("/addAccount")
  @Admin
  public ApiResult<AddAccountDto.Response> addAccount(
      @RequestBody @Valid AddAccountDto.Request req
  ) {
    AddAccountDto.Response response = adminService.addAccount(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "전체 매장 조회")
  @GetMapping("/getInstitutes")
  @Admin
  public ApiResult<List<GetInstituteDto.Response>> getInstitutes() {
    List<GetInstituteDto.Response> response = adminService.getInstitutes();
    return ApiResult.success(response);
  }

  @Operation(summary = "계정 수정")
  @PatchMapping("/updateAccount")
  @Admin
  public ApiResult<UpdateAccountDto.Response> updateAccount(
      @RequestBody @Valid UpdateAccountDto.Request req) {
    UpdateAccountDto.Response response = adminService.updateAccount(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "계정 삭제")
  @DeleteMapping("/deleteAccount")
  @Admin
  public ApiResult<?> lockAccount(@RequestBody Long accountId) {
    adminService.lockAccount(accountId);
    return ApiResult.success(true);
  }

  @Operation(summary = "로그인")
  @PostMapping("/login")
  @PermitAll
  public ApiResult<Boolean> login(@RequestBody @Valid LoginDto.Request req) {
    adminService.login(req);
    return ApiResult.success(true);
  }
}
