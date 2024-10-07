package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

public class OdsayException extends CustomException {

    public OdsayException(Status status) {
        super(status);
    }

    public OdsayException(String message, Status status) {
        super(message, status);
    }
}
