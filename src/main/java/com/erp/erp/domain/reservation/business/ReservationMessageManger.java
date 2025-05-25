package com.erp.erp.domain.reservation.business;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMessageManger {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  public void sendPushEvent(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqRouter.getNotificationPushEventExchange(),
        rabbitMqRouter.getNotificationPushEventQueueName(),
        message);
  }

}
