package com.erp.erp.global.config.log;

import com.erp.erp.global.util.ConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;

@Slf4j
public class LogPrinter {

  public void printRequestLog(String methodName, GenericMessage<?> message, String uuid) {
    String payloadJson = ConverterUtil.toJson(message.getPayload());
    printLog(methodName, uuid, payloadJson);
  }

  public void printResponseLog(Object result, String methodName, String uuid) {
    String value = ConverterUtil.toJson(result);
    printLog(methodName, uuid, value);
  }

  private void printLog(String methodName, String uuid, String value) {
    log.info("[{}:{}}] {}", methodName, uuid, value);
  }

}
