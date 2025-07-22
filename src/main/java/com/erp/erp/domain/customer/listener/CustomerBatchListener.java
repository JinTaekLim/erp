package com.erp.erp.domain.customer.listener;

import com.erp.erp.domain.batch.common.dto.UploadCustomerPhotoMessage;
import com.erp.erp.domain.batch.service.BatchConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerBatchListener {

  private final BatchConsumerService batchConsumerService;

  @RabbitListener(queues = "#{rabbitMqRouter.customerTemporaryPhotoQueueName}")
  public void temporaryPhoto(Message<?> message) {
    UploadCustomerPhotoMessage dto = (UploadCustomerPhotoMessage) message.getPayload();
    batchConsumerService.uploadCustomerPhotoBatch(dto);
  }
}
