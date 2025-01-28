package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;
import com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode;

public class OdsayTooManyRequestsException extends CustomException {

    public OdsayTooManyRequestsException() {
        super(OdsayErrorCode.TOO_MANY_REQUESTS);
    }

}
