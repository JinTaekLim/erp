package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqCustomerManger {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  public void sendUpdateCustomersCache(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqRouter.getCustomerUpdateCustomersCacheExchange(),
        rabbitMqRouter.getCustomerUpdateCustomersCacheQueueName(),
        message
    );
  }

}
