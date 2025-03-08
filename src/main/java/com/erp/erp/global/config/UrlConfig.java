package com.erp.erp.global.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlConfig {

  @Getter
  private List<String> url;

  private final UrlProperties urlProperties;

  @PostConstruct
  public void init() {
    if (isProdProfile()) {
      url = urlProperties.getProd();
    } else {
      url = urlProperties.getDev();
    }
  }

  @Value("${spring.profiles.active:default}")
  private String activeProfile;

  private boolean isProdProfile() {
    return "prod".equals(activeProfile);
  }
}
