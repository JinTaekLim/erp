package com.erp.erp.domain.admin.business;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqAdminManager {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  public void sendUpdatePlanCache(Message message){
    rabbitMqManager.sendMessage(
        rabbitMqRouter.getPlanUpdatePlanCacheExchange(),
        rabbitMqRouter.getPlanUpdatePlanCacheQueueName(),
        message
    );
  }
}
