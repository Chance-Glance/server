package com.example.mohago_nocar.transit.infrastructure.odsay.dto.response;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;

public record ODsayApiSuccessResponseWrapper(
        ODsayApiSuccessResponse apiResponse,
        RoutePoint departure,
        RoutePoint arrival
) {
    public static ODsayApiSuccessResponseWrapper of(ODsayApiSuccessResponse apiResponse, RoutePoint departure, RoutePoint arrival) {
        return new ODsayApiSuccessResponseWrapper(apiResponse, departure, arrival);
    }
}
