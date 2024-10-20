package com.erp.erp.domain.reservations.controller;

import com.erp.erp.domain.accounts.common.entity.Accounts;
import com.erp.erp.domain.auth.service.AuthService;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.service.InstitutesService;
import com.erp.erp.domain.reservations.common.dto.AddReservationsDto;
import com.erp.erp.domain.reservations.common.dto.GetDailyReservationsDto;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import com.erp.erp.domain.reservations.service.ReservationsService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
@Slf4j
public class ReservationsController {

  private final ReservationsService reservationsService;
  private final InstitutesService institutesService;
  private final AuthService authService;

  @Operation(summary = "예약 추가")
  @PostMapping("/addReservations")
  public ApiResult<?> addReservations(@Valid @RequestBody AddReservationsDto.Request req) {
    Accounts accounts = authService.getAccountsInfo();
    Institutes institutes = accounts.getInstitutes();
    Customers customers = institutesService.validateCustomerBelongsToInstitute(
        institutes,
        req.getCustomersId()
    );

    reservationsService.addReservations(req, customers);

    return null;
  }

  @Operation(summary = "하루 예약 조회")
  @GetMapping("/getDailyReservations")
  public ApiResult<List<GetDailyReservationsDto.Response>> getDailyReservations(
      @RequestParam LocalDate day
  ) {
    Accounts accounts = authService.getAccountsInfo();
    Institutes institutes = accounts.getInstitutes();
    List<Reservations> reservationsList = reservationsService.getDailyReservations(day, institutes);

    List<GetDailyReservationsDto.Response> response = reservationsList.stream()
        .map(reservations -> GetDailyReservationsDto.Response.builder()
            .reservationsId(reservations.getId())
            .startTime(LocalTime.from(reservations.getStartTime()))
            .endTime(LocalTime.from(reservations.getEndTime()))
            .name(reservations.getCustomers().getName())
            .build()
        )
        .toList();

    return ApiResult.success(response);
  }

}
