package com.example.mohago_nocar.global.common.exception;

import static com.example.mohago_nocar.global.common.exception.GlobalStatus.INTERNAL_SERVER_ERROR;

public class InternalServerException extends CustomException{

    public InternalServerException() {
        super(INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String message) {
        super(message, INTERNAL_SERVER_ERROR);
    }
}
