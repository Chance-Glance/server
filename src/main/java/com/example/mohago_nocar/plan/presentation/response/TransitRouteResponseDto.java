package com.example.mohago_nocar.plan.presentation.response;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.domain.model.RoutePoint;
import com.example.mohago_nocar.transit.domain.model.TransitRouteWithSegments;
import lombok.Builder;

import java.util.List;

@Builder
public record TransitRouteResponseDto(
        LocationResponse departure,
        LocationResponse arrival,
        int totalTime,
        double totalDistance,
        List<SubPathResponseDto> subPaths
) {

    public static TransitRouteResponseDto of(
            Location festivalLocation,
            TransitRouteWithSegments transitRouteWithSegments
    ) {
        RoutePoint departure = transitRouteWithSegments.getTransitRoute().getDeparture();
        RoutePoint arrival = transitRouteWithSegments.getTransitRoute().getArrival();

        return TransitRouteResponseDto.builder()
                .departure(LocationResponse.of(
                        getPlaceName(festivalLocation, departure), departure.getLongitude(), departure.getLatitude()))
                .arrival(LocationResponse.of(
                        getPlaceName(festivalLocation, arrival), arrival.getLongitude(), arrival.getLatitude()))
                .totalTime(transitRouteWithSegments.getTotalTime())
                .totalDistance(transitRouteWithSegments.getTotalDistance())
                .subPaths(transitRouteWithSegments.getOrderedRouteSegments().stream()
                        .map(routeSegment ->
                                switch (routeSegment.getTrafficType()) {
                                    case BUS -> BusPathResponseDto.of(routeSegment);
                                    case WALK -> WalkPathResponseDto.of(routeSegment);
                                    case SUBWAY -> SubwayPathResponseDto.of(routeSegment);
                                })
                        .toList())
                .build();
    }


    private static String getPlaceName(Location festivalLocation, RoutePoint routePoint) {
        if (festivalLocation.getLatitude() == routePoint.getLatitude()
                && festivalLocation.getLongitude() == routePoint.getLongitude()) {
            return festivalLocation.getPlaceName();
        }

        return routePoint.getName();
    }

}
