package com.erp.erp.global.rabbitMq;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RabbitMqMapper {

  private final String customerExchange;
  private final String reservationExchange;

  private final String addCustomerQueueName;
  private final String addReservationQueueName;
  private final String updateReservationQueueName;

  public RabbitMqMapper(RabbitMqProperties rabbitMqProperties) {
    this.customerExchange = rabbitMqProperties.getQueues().get(0).getExchange();
    this.reservationExchange = rabbitMqProperties.getQueues().get(1).getExchange();

    this.addCustomerQueueName = rabbitMqProperties.getQueues().get(0).getName().get(0);
    this.addReservationQueueName = rabbitMqProperties.getQueues().get(1).getName().get(0);
    this.updateReservationQueueName = rabbitMqProperties.getQueues().get(1).getName().get(1);
  }

}
