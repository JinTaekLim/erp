package com.erp.erp.global.properties;

import com.erp.erp.global.rabbitMq.connect.RabbitMqConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportServerRouter {

  private final RabbitMqConnectionManager rabbitMqConnectionManager;
  private final SupportServerApiProperties supportServerApiProperties;



  public String getOwnerNotificationConnectUrl(Long instituteId, String authKey) {
    String serverUrl = rabbitMqConnectionManager.getRandomSupportServerUrl();
    String apiUrl = supportServerApiProperties.getConnect();
    return serverUrl + apiUrl + instituteId + "?authKey=" + authKey;
  }
}
