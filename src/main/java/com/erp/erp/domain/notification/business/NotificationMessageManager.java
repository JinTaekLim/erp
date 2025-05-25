package com.erp.erp.domain.notification.business;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMessageManager {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  public String sendAndReceiveCreateAuth(Message message) {
    Object object = rabbitMqManager.sendAndReceiveMessage(
        rabbitMqRouter.getCreateAuthExchange(),
        rabbitMqRouter.getCreateAuthQueueName(),
        message
    );
    return object.toString();
  }
}
