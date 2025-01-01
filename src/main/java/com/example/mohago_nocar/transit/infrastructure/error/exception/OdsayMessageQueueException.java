package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

public class OdsayMessageQueueException extends CustomException {

    public OdsayMessageQueueException(Status status) {
        super(status);
    }

    public OdsayMessageQueueException(String message, Status status) {
        super(message, status);
    }

}
