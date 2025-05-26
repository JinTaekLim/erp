package com.erp.erp.global.config.log;

import com.erp.erp.global.util.ConverterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogPrinter {

  public void printRequestLog(String method, String url, String contentType, String body, String ip) {
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    log.info("Request : {} uri=[{}] content-type=[{}], body=[{}], client-ip=[{}]", method, url, contentType, body, ip);
  }

  public void printResponseLog(int status, String body, long startTime) {
    log.info("Response : {} body=[{}]", status, body);
    log.info("Request processed in {}ms", (System.currentTimeMillis() - startTime));
    log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
  }

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
