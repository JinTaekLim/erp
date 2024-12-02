package com.erp.erp.domain.reservations.controller;

import com.erp.erp.domain.institutes.service.InstitutesService;
import com.erp.erp.domain.payments.common.entity.Payments;
import com.erp.erp.domain.payments.service.PaymentsService;
import com.erp.erp.domain.reservations.common.dto.AddReservationsDto;
import com.erp.erp.domain.reservations.common.dto.DeleteReservationsDto;
import com.erp.erp.domain.reservations.common.dto.GetDailyReservationsDto;
import com.erp.erp.domain.reservations.common.dto.GetReservationCustomerDetailsDto;
import com.erp.erp.domain.reservations.common.dto.UpdatedReservationsDto;
import com.erp.erp.domain.reservations.common.dto.UpdatedSeatNumberDto;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.service.ReservationsService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@Tag(name = "reservations", description = "예약 관리")
@RequiredArgsConstructor
@Slf4j
public class ReservationsController {

  private final ReservationsService reservationsService;
  private final PaymentsService paymentsService;
  private final InstitutesService institutesService;

  @Operation(summary = "예약 추가")
  @PostMapping("/addReservations")
  public ApiResult<AddReservationsDto.Response> addReservations(
      @Valid @RequestBody AddReservationsDto.Request req) {
    Reservations reservations = reservationsService.addReservations(req);

    AddReservationsDto.Response response = AddReservationsDto.Response.builder()
        .reservationsId(reservations.getId())
        .customersId(reservations.getCustomers().getId())
        .startTime(reservations.getStartTime())
        .endTime(reservations.getEndTime())
        .memo(reservations.getMemo())
        .seatNumber(reservations.getSeatNumber())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "하루 예약 조회")
  @GetMapping("/getDailyReservations")
  public ApiResult<List<GetDailyReservationsDto.Response>> getDailyReservations(
      @RequestParam LocalDate day
  ) {
    List<Reservations> reservationsList = reservationsService.getDailyReservations(day);

    List<GetDailyReservationsDto.Response> response = reservationsList.stream()
        .map(reservations -> GetDailyReservationsDto.Response.builder()
            .reservationsId(reservations.getId())
            .startTime(LocalTime.from(reservations.getStartTime()))
            .endTime(LocalTime.from(reservations.getEndTime()))
            .seatNumber(reservations.getSeatNumber())
            .name(reservations.getCustomers().getName())
            .build()
        )
        .toList();

    return ApiResult.success(response);
  }

  @Operation(summary = "특정 시간 예약 조회")
  @GetMapping("/getReservationByTime")
  public ApiResult<List<GetDailyReservationsDto.Response>> getReservationByTime(@RequestParam LocalDateTime time) {
    List<Reservations> reservationsList = reservationsService.getReservationByTime(time);

    List<GetDailyReservationsDto.Response> response = reservationsList.stream()
        .map(reservations -> GetDailyReservationsDto.Response.builder()
            .reservationsId(reservations.getId())
            .startTime(LocalTime.from(reservations.getStartTime()))
            .endTime(LocalTime.from(reservations.getEndTime()))
            .name(reservations.getCustomers().getName())
            .seatNumber(reservations.getSeatNumber())
            .build()
        )
        .toList();

    return ApiResult.success(response);
  }



  @Operation(summary = "예약 수정")
  @PostMapping("/updatedReservation")
  public ApiResult<UpdatedReservationsDto.Response> updatedReservation(
      @Valid @RequestBody UpdatedReservationsDto.Request req) {
    Reservations reservations = reservationsService.updateReservation(req);

    UpdatedReservationsDto.Response response = UpdatedReservationsDto.Response.builder()
        .reservationsId(reservations.getId())
        .startTime(reservations.getStartTime().toLocalTime())
        .endTime(reservations.getEndTime().toLocalTime())
        .memo(reservations.getMemo())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "좌석 번호 변경")
  @PostMapping("/updatedSeatNumber")
  public ApiResult<UpdatedSeatNumberDto.Response> updatedSeatNumber(
      UpdatedSeatNumberDto.Request req
  ) {
    Reservations reservations = reservationsService.updatedSeatNumber(req);

    UpdatedSeatNumberDto.Response response = UpdatedSeatNumberDto.Response.builder()
        .reservationsId(reservations.getId())
        .seatNumber(reservations.getSeatNumber())
        .build();

    return ApiResult.success(response);
  }

  @Operation(summary = "예약 삭제")
  @PostMapping("/deleteReservations")
  public ApiResult<Boolean> deleteReservations(
      @Valid @RequestBody DeleteReservationsDto.Request req) {
    reservationsService.deleteReservations(req);
    return ApiResult.success(true);
  }

  @Operation(summary = "고객 예약 정보 조회")
  @GetMapping("getReservationCustomerDetails/{reservationsId}")
  public ApiResult<GetReservationCustomerDetailsDto.Response> getReservationCustomerDetails(
      @PathVariable long reservationsId
  ) {
    Reservations reservations = reservationsService.getReservationsForCurrentInstitute(
        reservationsId
    );
    Long customersId = reservations.getCustomers().getId();
    Payments payments = paymentsService.getCustomersPayments(customersId);

    GetReservationCustomerDetailsDto.Response response = GetReservationCustomerDetailsDto
        .Response
        .fromEntity(reservations, payments);

    return ApiResult.success(response);
  }
}

