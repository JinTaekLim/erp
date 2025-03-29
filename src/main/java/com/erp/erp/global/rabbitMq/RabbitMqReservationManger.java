package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqReservationManger {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqMapper rabbitMqMapper;

  public void sendAddReservation(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqMapper.getReservationExchange(),
        rabbitMqMapper.getAddReservationQueueName(),
        message);
  }

  public void sendUpdateReservation(Message message) {
    rabbitMqManager.sendMessage(
        rabbitMqMapper.getReservationExchange(),
        rabbitMqMapper.getUpdateReservationQueueName(),
        message);
  }

}
