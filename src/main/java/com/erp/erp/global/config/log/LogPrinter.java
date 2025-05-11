package com.erp.erp.global.config.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;

@Slf4j
public class LogPrinter {

  private final LogMapper logMapper = new LogMapper();

  public void printRequestLog(String methodName, GenericMessage<?> message, String uuid) {
    String payloadJson = logMapper.convertToJson(message.getPayload());
    printLog(methodName, uuid, payloadJson);
  }

  public void printResponseLog(Object result, String methodName, String uuid) {
    String value = logMapper.converToString(result);
    printLog(methodName, uuid, value);
  }

  private void printLog(String methodName, String uuid, String value) {
    log.info("[{}:{}}] {}", methodName, uuid, value);
  }

}
