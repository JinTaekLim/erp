package com.erp.erp.domain.notification.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.notification.business.NotificationServiceClient;
import com.erp.erp.global.properties.SupportServerRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final SupportServerRouter supportServer;
  private final AuthProvider authProvider;
  private final NotificationServiceClient notificationServiceClient;

  public String connect() {
    Long accountId = authProvider.getCurrentAccountId();
    Long instituteId = authProvider.getCurrentInstituteId();
    String authKey = notificationServiceClient.createAuth(accountId, instituteId);
    return supportServer.getOwnerNotificationConnectUrl(instituteId, authKey);
  }
}
