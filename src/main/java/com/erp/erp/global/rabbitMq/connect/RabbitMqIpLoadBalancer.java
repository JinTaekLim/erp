package com.erp.erp.global.rabbitMq.connect;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqIpLoadBalancer {

  // Support Server IP 랜덤 반환
  public String getRandomSupportIp(List<RabbitMqConnectionInfos> connections) {
    List<RabbitMqConnectionInfos> filteredConnections = filterConnectionsByUser(connections, "guest");
    validateConnections(filteredConnections);
    return selectRandomIp(filteredConnections);
  }

  // 유저 이름 필터링
  private List<RabbitMqConnectionInfos> filterConnectionsByUser(List<RabbitMqConnectionInfos> connections, String username) {
    return connections.stream()
        .filter(conn -> username.equals(conn.getUser()))
        .toList();
  }

  // 연결 정보가 없으면 예외 처리
  private void validateConnections(List<RabbitMqConnectionInfos> filteredConnections) {
    if (filteredConnections.isEmpty()) {
      throw new NoSuchElementException("사용 가능한 서버가 없습니다.");
    }
  }

  // 리스트에서 랜덤으로 IP를 선택하여 반환
  private String selectRandomIp(List<RabbitMqConnectionInfos> filteredConnections) {
    int randomIndex = ThreadLocalRandom.current().nextInt(filteredConnections.size());
    return filteredConnections.get(randomIndex).getHost();
  }
}