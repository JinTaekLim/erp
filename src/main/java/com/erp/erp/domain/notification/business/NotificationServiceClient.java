package com.erp.erp.domain.notification.business;

import com.erp.erp.domain.notification.common.mapper.NotificationMapper;
import com.erp.erp.domain.notification.dto.CreateAuthMessage;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import com.erp.erp.global.util.ConverterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationServiceClient {

  private final NotificationMessageManager sender;
  private final RabbitMqConverter converter;
  private final NotificationMapper mapper;

  public String createAuth(Long instituteId, Long accountId) {
    CreateAuthMessage.Request req = mapper.createAuthMessage(instituteId, accountId);
    Message message = converter.getMessage(req);

    // Message 를 Request 값으로, 결과를 JSON 으로 반환
    String json = sender.sendAndReceiveCreateAuth(message);
    CreateAuthMessage.Response response = ConverterUtil.toObject(json, CreateAuthMessage.Response.class);
    return response.getAuthKey();

  }

}
