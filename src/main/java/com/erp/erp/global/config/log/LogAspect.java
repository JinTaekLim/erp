package com.erp.erp.global.config.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

  private final ObjectMapper objectMapper;

  public LogAspect() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    SimpleModule module = new SimpleModule();
    module.addSerializer(byte[].class, new ByteArraySerializer());
    this.objectMapper.registerModule(module);
  }

  @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
  public void rabbitListenerMethods() {}


  @Before("rabbitListenerMethods()")
  public void logBefore(JoinPoint joinPoint) {
    String uuid = UUID.randomUUID().toString();

    logMethodName(joinPoint, uuid);
    logParameters(joinPoint, uuid);
  }



  private void logMethodName(JoinPoint joinPoint, String uuid) {
    String methodName = joinPoint.getSignature().getName();
    log.info("[{}] Method: [{}]", uuid, methodName);
  }

  private void logParameters(JoinPoint joinPoint, String uuid) {
    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof GenericMessage<?> message) {
        logPayload(message, uuid);
      }
    }
  }

  private void logPayload(GenericMessage<?> message, String uuid) {
    String payloadJson = convertToJson(message.getPayload());
    log.info("[{}] Payload: {}", uuid, payloadJson);
  }

  private String convertToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("Error serializing object to JSON: " + e.getMessage());
    }
  }
}
