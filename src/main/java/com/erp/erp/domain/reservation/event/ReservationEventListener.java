package com.erp.erp.domain.reservation.event;

import com.erp.erp.domain.reservation.business.PushUpdateReservationEvent;
import com.erp.erp.domain.reservation.business.ReservationMessageManger;
import com.erp.erp.domain.reservation.common.dto.PushEventUpdateReservationMessage;
import com.erp.erp.domain.reservation.common.mapper.ReservationEventMapper;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

  private final ReservationEventMapper reservationMapper;
  private final RabbitMqConverter converter;
  private final ReservationMessageManger sender;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUpdateUpdateReservationEvent(PushUpdateReservationEvent event) {
    PushEventUpdateReservationMessage req = reservationMapper.updateReservationMessage(event);
    Message message = converter.getMessage(req);
    sender.sendPushEvent(message);
  }
}
