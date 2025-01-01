package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;
import com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode;

import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.ROUTE_SEGMENT_NOT_FOUNT;

public class RouteSegmentNotFoundException extends CustomException {

    public RouteSegmentNotFoundException() {
        super(ROUTE_SEGMENT_NOT_FOUNT);
    }

    public RouteSegmentNotFoundException(String message, Status status) {
        super(message, status);
    }
}

