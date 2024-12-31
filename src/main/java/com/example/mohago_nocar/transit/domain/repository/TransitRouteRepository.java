package com.example.mohago_nocar.transit.domain.repository;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TransitRoute;

public interface TransitRouteRepository  {

    TransitRoute findByDepartureAndArrival(RoutePoint from, RoutePoint to);

    TransitRoute save(TransitRoute transitRoute);
}

