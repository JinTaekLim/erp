package com.erp.erp.global.notification;

import com.erp.erp.global.restTemplate.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendNotification {

  private final NotificationProperties notificationProperties;
  private final RestTemplateUtil restTemplateUtil = new RestTemplateUtil();

  public void sendError(String message) {
    String url = notificationProperties.getDiscordError();
    restTemplateUtil.sendPost(url, message);
  }

}
