package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

public class OdsayBadRequestException extends CustomException {

    public OdsayBadRequestException(Status status) {
        super(status);
    }

    public OdsayBadRequestException(String message, Status status) {
        super(message, status);
    }

}
