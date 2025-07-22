package com.erp.erp.domain.batch.sender;

import com.erp.erp.global.rabbitMq.RabbitMqManager;
import com.erp.erp.global.rabbitMq.RabbitMqRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchSender {

  private final RabbitMqManager rabbitMqManager;
  private final RabbitMqRouter rabbitMqRouter;

  public void sendUploadTemporaryPhoto(Object object) {
    Message message = rabbitMqManager.getMessage(object);
    rabbitMqManager.sendMessage(
        rabbitMqRouter.getCustomerTemporaryPhotoExchange(),
        rabbitMqRouter.getCustomerTemporaryPhotoQueueName(),
        message
    );
  }

}
