package com.erp.erp.global.config.log;

import java.util.UUID;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

  private final LogPrinter logPrinter = new LogPrinter();


  @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
  public void rabbitListenerMethods() {}



  @Around("rabbitListenerMethods()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

    // 고유값 생성
    String uuid = UUID.randomUUID().toString();

    // 메소드명 추출
    String methodName = joinPoint.getSignature().getName();

    // 요청 값 로깅
    logMessagePayload(joinPoint, methodName, uuid);

    // 메소드 실행
    Object result = joinPoint.proceed();

    // 응답 코드 로깅
    logPrinter.printResponseLog(result, methodName, uuid);


    return result;
  }


  private void logMessagePayload(JoinPoint joinPoint, String methodName, String uuid) {
    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof GenericMessage<?> message) {
        logPrinter.printRequestLog(methodName, message, uuid);
      }
    }
  }



}
