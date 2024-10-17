package com.example.mohago_nocar.global.common.exception;

import static com.example.mohago_nocar.global.common.exception.GlobalStatus.BAD_REQUEST;

public class InvalidValueException extends CustomException{

    public InvalidValueException() {
        super(BAD_REQUEST);
    }

    public InvalidValueException(Status status) {
        super(status);
    }
}
