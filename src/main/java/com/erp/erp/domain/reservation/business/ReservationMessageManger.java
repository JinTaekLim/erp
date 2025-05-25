package com.erp.erp.domain.reservation.business;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMessageManger {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqMapper rabbitMqMapper;

  public void sendPushEvent(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqMapper.getNotificationPushEventExchange(),
        rabbitMqMapper.getNotificationPushEventQueueName(),
        message);
  }

}
