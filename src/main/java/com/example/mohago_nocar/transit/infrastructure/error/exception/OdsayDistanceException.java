package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;

import static com.example.mohago_nocar.transit.infrastructure.error.code.OdsayErrorCode.POINTS_WITHIN_DISTANCE;

public class OdsayDistanceException extends CustomException {

    public OdsayDistanceException() {
        super(POINTS_WITHIN_DISTANCE);
    }
}
