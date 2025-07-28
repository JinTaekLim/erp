package com.erp.erp.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitMqListenerManager {

  private final RabbitListenerEndpointRegistry registry;

  public void stopAllListeners() {
    for (MessageListenerContainer container : registry.getListenerContainers()) {
      if (container.isRunning()) {
        container.stop();
      }
    }
  }

  public void startAllListeners() {
    for (MessageListenerContainer container : registry.getListenerContainers()) {
      if (!container.isRunning()) {
        container.start();
      }
    }
  }

}
