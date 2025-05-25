package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqCustomerManger {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqMapper rabbitMqMapper;

  public void sendUpdateCustomersCache(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqMapper.getCustomerUpdateCustomersCacheExchange(),
        rabbitMqMapper.getCustomerUpdateCustomersCacheQueueName(),
        message
    );
  }

}
