package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.AddReservationMessageDto;
import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.dto.UpdateReservationMessageDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import com.erp.erp.global.rabbitMq.RabbitMqReservationManger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationSender {

  private final RabbitMqReservationManger sender;
  private final RabbitMqConverter converter;

  public void sendAddReservation(Account account, Customer customer, PendingReservationDto pendingReservation, AddReservationDto.Request req) {
    AddReservationMessageDto dto = AddReservationMessageDto.builder()
        .account(account)
        .customer(customer)
        .pendingReservation(pendingReservation)
        .req(req)
        .build();
    Message message = converter.getMessage(dto);
    sender.sendAddReservation(message);
  }

  public void sendUpdateReservation(Reservation newReservation, ReservationCache newReservationCache, List<Progress> newProgress, PendingReservationDto pendingReservation) {
    UpdateReservationMessageDto dto = UpdateReservationMessageDto.builder()
        .reservation(newReservation)
        .reservationCache(newReservationCache)
        .progress(newProgress)
        .pendingReservation(pendingReservation)
        .build();
    Message message = converter.getMessage(dto);
    sender.sendUpdateReservation(message);
  }
}
