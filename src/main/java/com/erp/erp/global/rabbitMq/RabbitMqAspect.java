package com.erp.erp.global.rabbitMq;

import com.erp.erp.global.rabbitMq.connect.RabbitMqConnectionManager;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class RabbitMqAspect {

  private final RabbitMqListenerCacheManager rabbitMqListenerCacheManager;
  private final RabbitMqListenerManager rabbitMqListenerManager;
  private final RabbitMqConnectionManager connectionManager;

  private final RabbitMqCalculator calculator = new RabbitMqCalculator();

  @Pointcut("execution(* com.erp.erp.global.rabbitMq.RabbitMqManager.sendMessage(..))")
  public void rabbitSenderMethods() {}

  @Around("rabbitSenderMethods()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

    // 캐시가 N분 이내에 업데이트 되었다면, 별도의 처리 없이 메소드 실행
    LocalDateTime cacheUpdateAt = rabbitMqListenerCacheManager.getUpdatedAt();
    boolean status = calculator.isWithinNMinutes(cacheUpdateAt, 5);

    if (status) {
      return joinPoint.proceed();
    }

    // 캐시가 N분 이내에 업데이트 되지 않았을 경우
    // 1. 보조 서버가 동작 중인지 확인
    String url = connectionManager.getRandomSupportServerUrl();

    if (url == null) {
      // 1-1. 보조 서버 동작 시, 메인 서버의 Listener 를 비활성화 시켜 메세지를 수신하지 못 하도록 변경
      rabbitMqListenerManager.startAllListeners();
    } else {
      // 1-2. 보조 서버 동작 시, 메인 서버의 Listener 를 활성화 시켜 메세지를 수신하도록 변경
      rabbitMqListenerManager.stopAllListeners();
    }
    // 2. 캐시를 업데이트
    rabbitMqListenerCacheManager.updateAt();

    // 3. 메소드 실행
    return joinPoint.proceed();
  }

}
