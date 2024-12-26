package com.erp.erp.domain.reservation.controller;

import com.erp.erp.domain.reservation.common.dto.*;
import com.erp.erp.domain.reservation.common.entity.Reservation;
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
    Reservation reservation = reservationService.addReservations(req);
    AddReservationDto.Response response = AddReservationDto.Response.fromEntity(reservation);

    return ApiResult.success(response);
  }

  @Operation(summary = "하루 예약 조회")
  @GetMapping("/getDailyReservations")
  public ApiResult<List<GetDailyReservationDto.Response>> getDailyReservations(
      @RequestParam LocalDate day
  ) {
    List<Reservation> reservationList = reservationService.getDailyReservations(day);

    List<GetDailyReservationDto.Response> response = reservationList.stream()
        .map(reservations -> GetDailyReservationDto.Response.builder()
            .reservationId(reservations.getId())
            .startTime(reservations.getStartTime())
            .endTime(reservations.getEndTime())
            .seatNumber(reservations.getSeatNumber())
            .name(reservations.getCustomer().getName())
            .build()
        )
        .toList();

    return ApiResult.success(response);
  }

  @Operation(summary = "특정 시간 예약 조회")
  @GetMapping("/getReservationByTime")
  public ApiResult<List<GetDailyReservationDto.Response>> getReservationByTime(
      @RequestParam LocalDateTime time) {
    List<Reservation> reservationList = reservationService.getReservationByTime(time);

    List<GetDailyReservationDto.Response> response = reservationList.stream()
        .map(reservations -> GetDailyReservationDto.Response.builder()
            .reservationId(reservations.getId())
            .startTime(reservations.getStartTime())
            .endTime(reservations.getEndTime())
            .name(reservations.getCustomer().getName())
            .seatNumber(reservations.getSeatNumber())
            .build()
        )
        .toList();

    return ApiResult.success(response);
  }


  @Operation(summary = "예약 수정")
  @PostMapping("/updatedReservation")
  public ApiResult<UpdatedReservationDto.Response> updatedReservation(
      @Valid @RequestBody UpdatedReservationDto.Request req) {
    Reservation reservation = reservationService.updateReservation(req);

    UpdatedReservationDto.Response response = UpdatedReservationDto.Response.fromEntity(
        reservation);

    return ApiResult.success(response);
  }

  @Operation(summary = "좌석 번호 변경")
  @PostMapping("/updatedSeatNumber")
  public ApiResult<UpdatedSeatNumberDto.Response> updatedSeatNumber(
      UpdatedSeatNumberDto.Request req
  ) {
    Reservation reservation = reservationService.updatedSeatNumber(req);

    UpdatedSeatNumberDto.Response response = UpdatedSeatNumberDto.Response.builder()
        .reservationId(reservation.getId())
        .seatNumber(reservation.getSeatNumber())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "예약 삭제")
  @PostMapping("/deleteReservation")
  public ApiResult<Boolean> deleteReservation(
      @Valid @RequestBody DeleteReservationDto.Request req) {
    reservationService.deleteReservations(req);
    return ApiResult.success(true);
  }

  @Operation(summary = "고객 예약 정보 조회")
  @GetMapping("getReservationCustomerDetails/{reservationId}")
  public ApiResult<GetReservationCustomerDetailsDto.Response> getReservationCustomerDetails(
      @PathVariable long reservationId
  ) {
    GetReservationCustomerDetailsDto.Response response = reservationService.getReservationsForCurrentInstitute(
        reservationId
    );

    return ApiResult.success(response);
  }
}

