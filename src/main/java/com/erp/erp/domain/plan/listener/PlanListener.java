package com.erp.erp.domain.plan.listener;

import com.erp.erp.domain.plan.service.PlanService;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanListener {

  private final PlanService planService;
  private final RabbitMqRouter rabbitMqRouter;

  @RabbitListener(queues = "#{rabbitMqRouter.planUpdatePlanCacheQueueName}")
  public void updatePlanCache() {
    planService.updatePlanCache();
  }
}
