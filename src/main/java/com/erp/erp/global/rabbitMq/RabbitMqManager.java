package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMqManager {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(String exchange, String routingKey, Message message) {
    rabbitTemplate.convertAndSend(exchange, routingKey, message);
  }

  public Message getMessage(Object object) {
    return rabbitTemplate.getMessageConverter().toMessage(object, new MessageProperties());
  }
}
