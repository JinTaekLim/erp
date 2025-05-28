package com.erp.erp.global.rabbitMq.connect;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "rabbitmqClient", url = "${spring.rabbitmq.managementUrl}")
public interface RabbitMqClient {

  @GetMapping("/api/connections")
  List<RabbitMqConnectionInfos> getConnectionInfos(
      @RequestHeader("Authorization") String authHeader
  );


}
