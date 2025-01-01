package com.example.mohago_nocar.transit.infrastructure.messaging;

import com.example.mohago_nocar.transit.infrastructure.messaging.consumer.ApiRequestEventConsumer;
import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import com.example.mohago_nocar.transit.infrastructure.messaging.producer.ApiRequestEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransitRouteEventManager {

    @Value("${spring.rabbitmq.queue.odsay.dlq}")
    private String dlqName;

    private final ApiRequestEventConsumer eventConsumer;
    private final ApiRequestEventProducer eventProducer;

    public OdsayApiRequestEvent readFromDeferredQueue() {
        return eventConsumer.readFromDeferredQueue();
    }

    public void sendToPriorityQueue(OdsayApiRequestEvent message) {
        eventProducer.sendMessageToPriorityQueue(message);
    }

    public void sendToDeferredQueue(OdsayApiRequestEvent message) {
        eventProducer.sendMessageToDeferredQueue(message);
    }

    public void sendToDLQ(OdsayApiRequestEvent message) {
        eventProducer.sendMessageToDLQ(message);
    }

}
