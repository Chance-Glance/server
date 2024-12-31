package com.example.mohago_nocar.transit.domain.service;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;
import com.example.mohago_nocar.transit.domain.model.TransitRouteWithSegments;
import com.example.mohago_nocar.transit.infrastructure.odsay.dto.response.ODsayApiResponse;

public interface TransitUseCase {

    TransitRouteWithSegments findTransitRouteWithSegments(Location from, Location to);

    TransitRoute findTransitRoute(Location from, Location to);

    void saveTransitRouteWithSegments(ODsayApiResponse apiResponse, RoutePoint departure, RoutePoint arrival);

}
