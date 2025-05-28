package com.erp.erp.global.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SupportServerApiProperties {

  @Value("${support-server.api.connect}")
  private String connect;

}
