package com.erp.erp.domain.institute.controller;

import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.service.InstituteService;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/institute")
@Tag(name = "institute", description = "매장 관리")
@RequiredArgsConstructor
@Slf4j
public class InstituteController {

  private final InstituteService instituteService;

  @Operation(summary = "예약 가능한 총 갯수 변경")
  @PostMapping("/updateTotalSeat")
  public ApiResult<UpdateTotalSeatDto.Response> updateTotalSpots(
      @Valid @RequestBody UpdateTotalSeatDto.Request req
  ) {
    UpdateTotalSeatDto.Response response = instituteService.updateTotalSpots(req);
    return ApiResult.success(response);
  }

}
