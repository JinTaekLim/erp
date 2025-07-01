package com.erp.erp.domain.admin.event;

import com.erp.erp.domain.admin.business.RabbitMqAdminManager;
import com.erp.erp.domain.admin.common.dto.UpdatePlanCacheEvent;
import com.erp.erp.domain.admin.common.dto.UpdatePlanCacheMessageDto;
import com.erp.erp.domain.admin.common.mapper.AdminEventMapper;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AdminEventListener {

  private final RabbitMqAdminManager sender;
  private final RabbitMqConverter rabbitMqConverter;
  private final AdminEventMapper adminMapper;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUpdatePlanCacheEvent(UpdatePlanCacheEvent event) {
    UpdatePlanCacheMessageDto dto = adminMapper.updatePlanCacheEvent(event);
    Message message = rabbitMqConverter.getMessage(dto);
    sender.sendUpdatePlanCache(message);
  }
}
