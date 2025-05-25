package com.erp.erp.global.rabbitMq;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RabbitMqRouter {

  private final static String CUSTOMER_UPDATE_CUSTOMERS_CACHE = "customer-update-customers-cache";
  private final static String NOTIFICATION_CREATE_AUTH = "notification-create-auth";
  private final static String NOTIFICATION_PUSH_EVENT = "notification-push-event";

  private final String customerUpdateCustomersCacheQueueName;
  private final String customerUpdateCustomersCacheExchange;

  private final String createAuthQueueName;
  private final String createAuthExchange;

  private final String notificationPushEventQueueName;
  private final String notificationPushEventExchange;

  public RabbitMqRouter(RabbitMqProperties rabbitMqProperties) {
    this.customerUpdateCustomersCacheQueueName = rabbitMqProperties.getName(CUSTOMER_UPDATE_CUSTOMERS_CACHE);
    this.customerUpdateCustomersCacheExchange = rabbitMqProperties.getExchange(CUSTOMER_UPDATE_CUSTOMERS_CACHE);

    this.createAuthQueueName = rabbitMqProperties.getName(NOTIFICATION_CREATE_AUTH);
    this.createAuthExchange = rabbitMqProperties.getExchange(NOTIFICATION_CREATE_AUTH);

    this.notificationPushEventQueueName = rabbitMqProperties.getName(NOTIFICATION_PUSH_EVENT);
    this.notificationPushEventExchange = rabbitMqProperties.getExchange(NOTIFICATION_PUSH_EVENT);

  }

}
