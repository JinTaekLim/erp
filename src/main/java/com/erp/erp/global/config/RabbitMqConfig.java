package com.erp.erp.global.config;

import com.erp.erp.global.rabbitMq.RabbitMqProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

  private final RabbitMqProperties rabbitMqProperties;

  @Bean
  public Declarables rabbitDeclarables() {
    List<Declarable> declarable = rabbitMqProperties.getQueues().stream()
        .flatMap(q -> q.getName().stream()
            .map(queueName -> createQueueAndBinding(q.getExchange(), queueName)))
        .flatMap(List::stream)
        .toList();

    return new Declarables(declarable);
  }

  private List<Declarable> createQueueAndBinding(String exchangeName, String queueName) {
    Queue queue = new Queue(queueName, true);
    DirectExchange exchange = new DirectExchange(exchangeName);
    Binding binding = BindingBuilder.bind(queue)
        .to(exchange)
        .with(queueName);

    return List.of(exchange, queue, binding);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    return rabbitTemplate;
  }

  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
