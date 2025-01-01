package com.example.mohago_nocar.transit.infrastructure.messaging.producer;

import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OdsayApiRequestEventProducer implements ApiRequestEventProducer {

    private static final int NO_RETRY = 0;
    private static final int RETRY_ONCE = 1;
    private static final int RETRY_TWICE = 2;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessageToPriorityQueue(OdsayApiRequestEvent event) {
        if (event.isMaxRetry()) {
            log.warn("Max retry attempts reached");
            sendMessageToDLQ(event);
        }
        else {
            rabbitTemplate.convertAndSend("odsay.direct.exchange", "odsay.priority.key", event,
                    msg -> {
                        int priority = getPriorityBy(event.getRetryCount());
                        msg.getMessageProperties().setPriority(priority);
                        return msg;
                    });
        }
    }

    @Override
    public void sendMessageToDeferredQueue(OdsayApiRequestEvent event) {
        rabbitTemplate.convertAndSend("odsay.direct.exchange", "odsay.deferred.key", event);
    }

    @Override
    public void sendMessageToDLQ(OdsayApiRequestEvent event) {
        rabbitTemplate.convertAndSend("odsay.direct.exchange", "odsay.dlq.key", event);
    }

    private int getPriorityBy(Integer retry) {
         return switch (retry){
            case NO_RETRY -> 0;
            case RETRY_ONCE -> 1;
            case RETRY_TWICE -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + retry);
         };
    }

}
