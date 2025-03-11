package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.AddReservationMessageDto;
import com.erp.erp.global.rabbitMq.RabbitMqManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationSender {

  private final RabbitMqManager rabbitMqManager;

  public void sendAddReservation(Account account, Customer customer, AddReservationDto.Request req) {
    AddReservationMessageDto dto = AddReservationMessageDto.builder()
        .account(account)
        .customer(customer)
        .req(req)
        .build();
    Message message = rabbitMqManager.getMessage(dto);
    rabbitMqManager.sendReservationMessage(message);
  }
}
