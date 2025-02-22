package com.erp.erp.domain.reservation.controller;

import com.erp.erp.domain.reservation.common.dto.*;
import com.erp.erp.domain.reservation.service.ReservationService;
import com.erp.erp.global.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "reservation", description = "예약 관리")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

  private final ReservationService reservationService;

  @Operation(summary = "예약 추가")
  @PostMapping("/addReservation")
  public ApiResult<AddReservationDto.Response> addReservation(
      @Valid @RequestBody AddReservationDto.Request req) {
    AddReservationDto.Response response = reservationService.addReservations(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "하루 예약 조회")
  @GetMapping("/getDailyReservations")
  public ApiResult<List<GetDailyReservationDto.Response>> getDailyReservations(
      @RequestParam("day") LocalDate day
  ) {
    List<GetDailyReservationDto.Response> response = reservationService.getDailyReservations(day);
    return ApiResult.success(response);
  }

  @Operation(summary = "특정 시간 예약 조회")
  @GetMapping("/getReservationByTime")
  public ApiResult<List<GetDailyReservationDto.Response>> getReservationByTime(
      @RequestParam("time") LocalDateTime time) {
    List<GetDailyReservationDto.Response> response = reservationService.getReservationByTime(time);
    return ApiResult.success(response);
  }


  @Operation(summary = "예약 수정")
  @PutMapping("/updatedReservation")
  public ApiResult<UpdatedReservationDto.Response> updatedReservation(
      @Valid @RequestBody UpdatedReservationDto.Request req) {
    UpdatedReservationDto.Response response = reservationService.updateReservation(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "좌석 번호 변경")
  @PutMapping("/updatedSeatNumber")
  public ApiResult<UpdatedSeatNumberDto.Response> updatedSeatNumber(
      @Valid @RequestBody UpdatedSeatNumberDto.Request req
  ) {
    UpdatedSeatNumberDto.Response response = reservationService.updatedSeatNumber(req);
    return ApiResult.success(response);
  }

  @Operation(summary = "예약 삭제")
  @DeleteMapping("/deleteReservation/{reservationId}")
  public ApiResult<Boolean> deleteReservation(
      @Valid @PathVariable("reservationId") Long reservationId) {
    reservationService.deleteReservations(reservationId);
    return ApiResult.success(true);
  }

  @Operation(summary = "고객 예약 정보 조회")
  @GetMapping("getReservationCustomerDetails/{reservationId}")
  public ApiResult<GetReservationCustomerDetailsDto.Response> getReservationCustomerDetails(
      @PathVariable long reservationId
  ) {
    GetReservationCustomerDetailsDto.Response response = reservationService.getReservationCustomerDetails(
        reservationId
    );
    return ApiResult.success(response);
  }
}

