package com.erp.erp.global.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SupportServerProperties {

  @Value("${support-server.url}")
  private String serverUrl;

  @Value("${support-server.api.connect}")
  private String connect;


  public String getOwnerNotificationConnectUrl(Long instituteId, String authKey) {
    return serverUrl + connect + instituteId + "?authKey=" + authKey;
  }
}
