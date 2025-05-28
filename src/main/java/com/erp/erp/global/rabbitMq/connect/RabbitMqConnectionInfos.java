package com.erp.erp.global.rabbitMq.connect;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RabbitMqConnectionInfos {

  // IP
  private String host;

  // 계정명
  private String user;

}
