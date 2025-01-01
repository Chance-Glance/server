package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

import static com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode.LIMIT_COUNT_FETCH_ERROR;

public class OdsayCounterException extends CustomException {

    public OdsayCounterException() {
        super(LIMIT_COUNT_FETCH_ERROR);
    }

    public OdsayCounterException(String message, Status status) {
        super(message, status);
    }

}
