package com.erp.erp.global.rabbitMq;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RabbitMqMapper {

  private final String customerQueueName;
  private final String reservationQueueName;

  public RabbitMqMapper(RabbitMqProperties rabbitMqProperties) {
    this.customerQueueName = rabbitMqProperties.getQueues().get(0).getName();
    this.reservationQueueName = rabbitMqProperties.getQueues().get(1).getName();
  }

}
