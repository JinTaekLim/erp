package com.erp.erp.global.rabbitMq.connect;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqConnectionManager {

  private final RabbitMqAuthProvider rabbitMqAuthProvider;
  private final RabbitMqClient client;
  private final RabbitMqIpLoadBalancer loadBalancer;
  private final RabbitMqUrlProvider urlProvider;


  public String getRandomSupportServerUrl() {
    String auth = rabbitMqAuthProvider.getAuthHeader();
    List<RabbitMqConnectionInfos> infos = client.getConnectionInfos(auth);
    String ip = loadBalancer.getRandomSupportIp(infos);
    return urlProvider.getUrl(ip);
  }



}
