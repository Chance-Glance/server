package com.example.mohago_nocar.transit.domain.repository;

import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import com.example.mohago_nocar.transit.domain.model.segment.WalkingSegment;

import java.util.List;

public interface RouteSegmentRepository {

    List<RouteSegment> findByTransitRouteId(Long transitRouteId);

    WalkingSegment saveWalkingSegment(WalkingSegment walkingSegment);

    void saveAll(List<RouteSegment> routeSegments);
}
