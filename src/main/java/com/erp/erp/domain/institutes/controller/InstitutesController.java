package com.erp.erp.domain.institutes.controller;

import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.service.InstitutesService;
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
@RequestMapping("/api/institutes")
@Tag(name = "institutes", description = "매장 관리")
@RequiredArgsConstructor
@Slf4j
public class InstitutesController {

  private final InstitutesService institutesService;

  @Operation(summary = "예약 가능한 총 갯수 변경")
  @PostMapping("/updateTotalSpots")
  public ApiResult<UpdateTotalSpotsDto.Response> updateTotalSpots(
      @Valid @RequestBody UpdateTotalSpotsDto.Request req
  ) {
    UpdateTotalSpotsDto.Response response = institutesService.updateTotalSpots(req);
    return ApiResult.success(response);
  }

}
