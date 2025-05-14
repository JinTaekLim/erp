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
import com.erp.erp.domain.reservation.common.mapper.ReservationSenderMapper;
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
  private final ReservationSenderMapper mapper;

  public void sendAddReservation(Account account, Customer customer, PendingReservationDto pendingReservation, AddReservationDto.Request req) {
    AddReservationMessageDto dto = mapper.toAddReservationMessageDto(account, customer, pendingReservation, req);
    Message message = converter.getMessage(dto);
    sender.sendAddReservation(message);
  }

  public void sendUpdateReservation(Reservation reservation, ReservationCache reservationCache, List<Progress> progress, PendingReservationDto pendingReservation) {
    UpdateReservationMessageDto dto = mapper.toUpdateReservationMessageDto(reservation, reservationCache, progress, pendingReservation);
    Message message = converter.getMessage(dto);
    sender.sendUpdateReservation(message);
  }
}
