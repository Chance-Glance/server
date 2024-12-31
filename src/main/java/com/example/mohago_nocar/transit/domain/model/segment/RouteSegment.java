package com.example.mohago_nocar.transit.domain.model.segment;

import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TrafficType;

public interface RouteSegment {

    TrafficType getTrafficType();
    int getSequence();
    RoutePoint getDeparture();
    RoutePoint getArrival();
    Long getTransitRouteId();
    void setTransitId(Long transitId);

}
