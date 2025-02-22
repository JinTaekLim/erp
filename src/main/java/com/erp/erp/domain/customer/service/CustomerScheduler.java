package com.erp.erp.domain.customer.service;

import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerScheduler {

  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;

  // 매정각 만료일자가 지난 회원의 상태 값을 변경하는 코드 필요

  private final int BEFORE_DAY = 7;

  // 1주일 전 등록한 모든 회원의 정보를 가져오고, 첫 예약 날짜부터 혹은 현재 날짜 기준으로 만료 기간 설정
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateExpired() {
    List<UpdateCustomerExpiredAtDto> customers = customerReader.findCustomersCreatedBeforeDays(BEFORE_DAY);
    List<UpdateCustomerExpiredAtDto.Request> requests = createExpiredRequests(customers);

    if (!requests.isEmpty()) {
      customerUpdater.updateExpiredAt(requests);
    }
  }

  private List<UpdateCustomerExpiredAtDto.Request> createExpiredRequests(List<UpdateCustomerExpiredAtDto> customers) {
    return customers.stream()
        .map(this::createRequest)
        .toList();
  }

  private UpdateCustomerExpiredAtDto.Request createRequest(UpdateCustomerExpiredAtDto customer) {
    LocalDate start = Optional.ofNullable(customer.getFirstReservationDate())
        .map(LocalDateTime::toLocalDate)
        .orElse(LocalDate.now());

    LocalDate expiredAt = start.plusDays(customer.getAvailablePeriod());

    return UpdateCustomerExpiredAtDto.Request.builder()
        .customerId(customer.getId())
        .expiredAt(expiredAt)
        .build();
  }

}
