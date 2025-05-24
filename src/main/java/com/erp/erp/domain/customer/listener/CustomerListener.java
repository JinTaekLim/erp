package com.erp.erp.domain.customer.listener;

import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheMessageDto;
import com.erp.erp.domain.customer.service.CustomerService;
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

  @RabbitListener(queues = "customer.updateCustomersCache")
  public void updateCustomersCache(Message<?> message) {
    UpdateCustomersCacheMessageDto dto = (UpdateCustomersCacheMessageDto) message.getPayload();
    customerService.updateCustomersCache(dto);
  }
//
//  private final RabbitMqMapper rabbitMqMapper;
//
//  @RabbitListener(queues = "#{rabbitMqMapper.addCustomerQueueName}")
//  public void addCustomerMessage(Message<?> message) {
//    AddCustomerMessageDto dto = (AddCustomerMessageDto) message.getPayload();
//    Account account = dto.getAccount();
//    Plan plan = dto.getPlan();
//    AddCustomerDto.Request req = dto.getReq();
//    byte[] file = dto.getFile();
//
//    customerService.addCustomer(account, plan, req, file);
//  }

}
