package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.AddCustomerMessageDto;
import com.erp.erp.global.rabbitMq.RabbitMqConverter;
import com.erp.erp.global.rabbitMq.RabbitMqCustomerManger;
import com.erp.erp.domain.plan.common.entity.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@RequiredArgsConstructor
public class CustomerSender {

  private final RabbitMqCustomerManger sender;
  private final RabbitMqConverter converter;

  public void sendAddCustomer(Account account, Plan plan, AddCustomerDto.Request req, MultipartFile file){
    try {
      byte[] bytes = (file == null) ? null : file.getBytes();

      AddCustomerMessageDto messageBody = AddCustomerMessageDto.builder()
          .req(req)
          .plan(plan)
          .file(bytes)
          .account(account)
          .build();

      Message message = converter.getMessage(messageBody);
      sender.sendAddCustomer(message);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

  }
}
