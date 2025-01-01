package com.example.mohago_nocar.transit.infrastructure.error.exception;

import com.example.mohago_nocar.global.common.exception.CustomException;
import com.example.mohago_nocar.global.common.exception.Status;

import static com.example.mohago_nocar.transit.infrastructure.error.code.TransitRouteErrorCode.TRANSIT_ROUTE_BATCH_ERROR;

public class OdsayBatchException extends CustomException {

    public OdsayBatchException(Status status) {
        super(TRANSIT_ROUTE_BATCH_ERROR);
    }

    public OdsayBatchException(String message, Status status) {
        super(message, status);
    }

}
