package com.erp.erp.domain.reservation.listener;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.AddReservationMessageDto;
import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationListener {

  private final ReservationService reservationService;

  @RabbitListener(queues = "${rabbitmq.queues[1].name}")
  public void addCustomerMessage(@Payload AddReservationMessageDto dto) {
    Account account = dto.getAccount();
    Customer customer = dto.getCustomer();
    PendingReservationDto pendingReservation = dto.getPendingReservation();
    AddReservationDto.Request req = dto.getReq();

    reservationService.addReservations(account, customer, pendingReservation, req);
  }

}
