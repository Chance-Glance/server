package com.example.mohago_nocar.transit.infrastructure.messaging.consumer;

import com.example.mohago_nocar.transit.infrastructure.messaging.persistence.OdsayApiRequestEvent;

public interface ApiRequestEventConsumer {

    OdsayApiRequestEvent readFromDeferredQueue();

}
