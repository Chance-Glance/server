package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;
import com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode;

public class OdsayServerException extends CustomException {

    public OdsayServerException() {
        super(OdsayErrorCode.ODSAY_SERVER_ERROR);
    }

    public OdsayServerException(String message, Status status) {
        super(message, status);
    }
}
