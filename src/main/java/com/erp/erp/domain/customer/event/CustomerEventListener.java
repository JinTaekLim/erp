package com.erp.erp.domain.customer.event;

import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheEvent;
import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheMessageDto;
import com.erp.erp.domain.customer.common.mapper.CustomerEventMapper;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import com.erp.erp.global.rabbitMq.RabbitMqCustomerManger;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CustomerEventListener {

  private final RabbitMqCustomerManger sender;
  private final RabbitMqConverter converter;
  private final CustomerEventMapper mapper;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUpdateCustomerCacheEvent(UpdateCustomersCacheEvent event) {
    UpdateCustomersCacheMessageDto messageBody = mapper.toUpdateCustomersCacheMessageDto(event);
    Message message = converter.getMessage(messageBody);
    sender.sendUpdateCustomersCache(message);
  }
}
