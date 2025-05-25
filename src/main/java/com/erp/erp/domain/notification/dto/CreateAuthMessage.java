package com.erp.erp.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class CreateAuthMessage {

  @Getter
  @Builder
  public static class Request {

    private Long instituteId;
    private Long accountId;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String authKey;

  }
}
