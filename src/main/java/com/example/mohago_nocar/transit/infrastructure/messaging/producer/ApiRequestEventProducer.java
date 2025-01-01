package com.example.mohago_nocar.transit.infrastructure.messaging.producer;

import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;

public interface ApiRequestEventProducer {

    void sendMessageToPriorityQueue(OdsayApiRequestEvent message);

    void sendMessageToDeferredQueue(OdsayApiRequestEvent message);

    void sendMessageToDLQ(OdsayApiRequestEvent message);

}
