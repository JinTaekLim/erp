package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqConverter {

  private final RabbitMqManager rabbitMqManager;

  public Message getMessage(Object object) {
    return rabbitMqManager.getMessage(object);
  }
}
