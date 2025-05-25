package com.erp.erp.domain.notification.controller;

import com.erp.erp.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "notification", description = "알림 관리")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "알림 서버 연결")
  @GetMapping("/connect")
  public RedirectView connect() {
    String url = notificationService.connect();
    return new RedirectView(url);
  }

}
