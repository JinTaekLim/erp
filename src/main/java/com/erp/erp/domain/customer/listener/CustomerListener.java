package com.erp.erp.domain.customer.listener;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.AddCustomerMessageDto;
import com.erp.erp.domain.customer.service.CustomerService;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.global.rabbitMq.RabbitMqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerListener {

  private final CustomerService customerService;
  private final RabbitMqMapper rabbitMqMapper;

  @RabbitListener(queues = "#{rabbitMqMapper.addCustomerQueueName}")
  public void addCustomerMessage(Message<?> message) {
    AddCustomerMessageDto dto = (AddCustomerMessageDto) message.getPayload();
    Account account = dto.getAccount();
    Plan plan = dto.getPlan();
    AddCustomerDto.Request req = dto.getReq();
    byte[] file = dto.getFile();

    customerService.addCustomer(account, plan, req, file);
  }

}
