package com.example.mohago_nocar.transit.domain.model;

import com.example.mohago_nocar.transit.domain.model.segment.RouteSegment;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class TransitRouteWithSegments {

    private TransitRoute transitRoute;
    private List<RouteSegment> routeSegments;

    public static TransitRouteWithSegments from(TransitRoute transitRoute, List<RouteSegment> segments) {
        return TransitRouteWithSegments.builder()
                .transitRoute(transitRoute)
                .routeSegments(segments)
                .build();
    }

    public List<RouteSegment> getOrderedRouteSegments() {
        return routeSegments.stream()
                .sorted(Comparator.comparingInt(RouteSegment::getSequence))
                .toList();
    }

    @Builder
    private TransitRouteWithSegments(TransitRoute transitRoute, List<RouteSegment> routeSegments) {
        this.transitRoute = transitRoute;
        this.routeSegments = routeSegments;
    }

    public int getTotalTime() {
        return transitRoute.getTotalTime();
    }

    public double getTotalDistance() {
        return transitRoute.getTotalDistance();
    }

}
