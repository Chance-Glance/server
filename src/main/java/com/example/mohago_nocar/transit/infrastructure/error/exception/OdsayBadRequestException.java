package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

import static com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode.POINTS_WITHIN_DISTANCE;

public class OdsayBadRequestException extends CustomException {

    public OdsayBadRequestException(Status status) {
        super(status);
    }
}
