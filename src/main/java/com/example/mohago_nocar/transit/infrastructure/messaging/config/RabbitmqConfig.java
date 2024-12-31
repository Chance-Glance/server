package com.example.mohago_nocar.transit.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.queue.odsay.priority}")
    private String odsayPriorityQueueName;

    @Value("${spring.rabbitmq.queue.odsay.dlq}")
    private String odsayDlqName;

    @Value("${spring.rabbitmq.queue.odsay.deferred}")
    private String odsayDeferredQueueName;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;


    /**
     * 라우팅 키를 기준으로 바운딩 된 큐로 라우팅
     */
    @Bean
    DirectExchange odsayDirectExchange() {
        return new DirectExchange("odsay.direct.exchange");
    }

    @Bean
    Queue odsayPriorityQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "odsay.direct.exchange");
        args.put("x-dead-letter-routing-key", "odsay.dlq.key");

        return QueueBuilder.durable(odsayPriorityQueueName)
                .maxPriority(2)
                .withArguments(args)
                .build();
    }

    // odsayDirectExchange로 들어온 메시지 중 "odsay.priority.key" 라우팅 키를 가진 메시지는 odsayPriorityQueue로 전송
    @Bean
    Binding priorityBinding(DirectExchange odsayDirectExchange, Queue odsayPriorityQueue) {
        return BindingBuilder
                .bind(odsayPriorityQueue)
                .to(odsayDirectExchange)
                .with("odsay.priority.key");
    }

    @Bean
    Queue odsayDeadLetterQueue() {
        return QueueBuilder.durable(odsayDlqName)
                .build();
    }

    // odsayDirectExchange로 들어온 메시지 중 "odsay.dlq.key" 라우팅 키를 가진 메시지는 odsayDLQ로 전송
    @Bean
    Binding dlqBinding(DirectExchange odsayDirectExchange, Queue odsayDeadLetterQueue) {
        return BindingBuilder
                .bind(odsayDeadLetterQueue)
                .to(odsayDirectExchange)
                .with("odsay.dlq.key");
    }

    @Bean
    Queue odsayDeferredQueue() {
        return QueueBuilder.durable(odsayDeferredQueueName)
                .build();
    }

    @Bean
    Binding deferredBinding(DirectExchange odsayDirectExchange, Queue odsayDeferredQueue) {
        return BindingBuilder
                .bind(odsayDeferredQueue)
                .to(odsayDirectExchange)
                .with("odsay.deferred.key");
    }

    @Bean
    ConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(1); // 기본 동시 컨슈머 수
        factory.setMaxConcurrentConsumers(1); // 최대 동시 컨슈머 수
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // 자동 ACK 모드
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);
//        factory.setPrefetchCount(10); // 한 번에 가져올 메시지 수
        return factory;
    }


    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
