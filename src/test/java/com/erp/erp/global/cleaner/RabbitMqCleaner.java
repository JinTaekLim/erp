package com.erp.erp.global.cleaner;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqCleaner {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void clear() {
    rabbitTemplate.destroy();
  }
}
