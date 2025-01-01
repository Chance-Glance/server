package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;
import com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode;

import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.TRANSIT_ROUTE_NOT_FOUND;

public class TransitRouteNotFoundException extends CustomException {

    public TransitRouteNotFoundException() {
        super(TRANSIT_ROUTE_NOT_FOUND);
    }

    public TransitRouteNotFoundException(String message, Status status) {
        super(message, status);
    }

}
