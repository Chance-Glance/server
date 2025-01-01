package com.example.mohago_nocar.transit.infrastructure.messaging.consumer;

import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OdsayApiRequestEventConsumer implements ApiRequestEventConsumer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.odsay.deferred}")
    private String deferredQueueName;

    @Override
    public OdsayApiRequestEvent readFromDeferredQueue() {
        // todo: 1. 적절한 timeoutMillis 값 찾기 2. 메시지 한 개씩만 꺼내오도록 할 것인지 결정
        return rabbitTemplate.receiveAndConvert(deferredQueueName, 500,
                new ParameterizedTypeReference<>() {});
    }

}
