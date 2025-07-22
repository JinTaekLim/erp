package com.erp.erp.global.rabbitMq;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RabbitMqRouter {

  private final static String CUSTOMER_UPDATE_CUSTOMERS_CACHE = "customer-update-customers-cache";
  private final static String NOTIFICATION_CREATE_AUTH = "notification-create-auth";
  private final static String NOTIFICATION_PUSH_EVENT = "notification-push-event";
  private final static String PLAN_UPDATE_PLAN_CACHE = "plan-update-plan-cache";

  private final String customerUpdateCustomersCacheQueueName;
  private final String customerUpdateCustomersCacheExchange;

  private final String createAuthQueueName;
  private final String createAuthExchange;

  private final String notificationPushEventQueueName;
  private final String notificationPushEventExchange;

  private final String planUpdatePlanCacheQueueName;
  private final String planUpdatePlanCacheExchange;


  // Batch
  private final static String CUSTOMER_TEMPORARY_PHOTO = "customer-temporary-photo";
  private final static String CUSTOMER_UPDATE_STATUS = "customer-update-status";
  private final static String CUSTOMER_UPDATE_EXPIRED_AT = "customer-update-expired-at";

  private final String customerTemporaryPhotoQueueName;
  private final String customerTemporaryPhotoExchange;

  private final String customerUpdateStatusQueueName;
  private final String customerUpdateStatusExchange;

  private final String customerUpdateExpiredAtQueueName;
  private final String customerUpdateExpiredAtExchange;

  public RabbitMqRouter(RabbitMqProperties rabbitMqProperties) {
    this.customerUpdateCustomersCacheQueueName = rabbitMqProperties.getName(CUSTOMER_UPDATE_CUSTOMERS_CACHE);
    this.customerUpdateCustomersCacheExchange = rabbitMqProperties.getExchange(CUSTOMER_UPDATE_CUSTOMERS_CACHE);

    this.createAuthQueueName = rabbitMqProperties.getName(NOTIFICATION_CREATE_AUTH);
    this.createAuthExchange = rabbitMqProperties.getExchange(NOTIFICATION_CREATE_AUTH);

    this.notificationPushEventQueueName = rabbitMqProperties.getName(NOTIFICATION_PUSH_EVENT);
    this.notificationPushEventExchange = rabbitMqProperties.getExchange(NOTIFICATION_PUSH_EVENT);

    this.planUpdatePlanCacheQueueName = rabbitMqProperties.getName(PLAN_UPDATE_PLAN_CACHE);
    this.planUpdatePlanCacheExchange = rabbitMqProperties.getExchange(PLAN_UPDATE_PLAN_CACHE);

    this.customerTemporaryPhotoQueueName = rabbitMqProperties.getName(CUSTOMER_TEMPORARY_PHOTO);
    this.customerTemporaryPhotoExchange = rabbitMqProperties.getExchange(CUSTOMER_TEMPORARY_PHOTO);

    this.customerUpdateStatusQueueName = rabbitMqProperties.getName(CUSTOMER_UPDATE_STATUS);
    this.customerUpdateStatusExchange = rabbitMqProperties.getExchange(CUSTOMER_UPDATE_STATUS);

    this.customerUpdateExpiredAtQueueName = rabbitMqProperties.getName(CUSTOMER_UPDATE_EXPIRED_AT);
    this.customerUpdateExpiredAtExchange = rabbitMqProperties.getExchange(CUSTOMER_UPDATE_EXPIRED_AT);
  }

}
