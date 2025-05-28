package com.erp.erp.domain.notification.business;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMessageManager {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  // note. 검증 과정 및 추출 과정 클래스 분리 필요
  public String sendAndReceiveCreateAuth(Message message) {
    Object object = rabbitMqManager.sendAndReceiveMessage(
        rabbitMqRouter.getCreateAuthExchange(),
        rabbitMqRouter.getCreateAuthQueueName(),
        message
    );

    if (object == null) {
      throw new IllegalStateException("서버의 응답이 없거나 오류가 발생했습니다.");
    }

    return object.toString();
  }
}
