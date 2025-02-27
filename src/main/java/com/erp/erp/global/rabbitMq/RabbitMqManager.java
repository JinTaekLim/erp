package com.erp.erp.global.rabbitMq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqManager {

  private final RabbitTemplate rabbitTemplate;
  private final String customerQueueName;

  public RabbitMqManager(RabbitMqProperties rabbitMqProperties, RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.customerQueueName = rabbitMqProperties.getQueues().get(0).getName();
  }

  public void sendCustomerMessage(Message message) {
    rabbitTemplate.convertAndSend(customerQueueName, message);
  }

  public Message getMessage(Object object) {
    return rabbitTemplate.getMessageConverter().toMessage(object, new MessageProperties());
  }
}
