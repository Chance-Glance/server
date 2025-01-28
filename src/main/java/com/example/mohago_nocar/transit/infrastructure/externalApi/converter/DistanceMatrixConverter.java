package com.example.mohago_nocar.transit.infrastructure.externalApi.converter;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.transit.infrastructure.externalApi.google.dto.response.RouteSpecification;
import com.example.mohago_nocar.transit.infrastructure.externalApi.google.dto.response.GoogleDistanceMatrixResponse;

import java.util.List;
import java.util.stream.IntStream;

public class DistanceMatrixConverter {

    public static List<RouteSpecification> convertMatrixToRouteSpecs(
            GoogleDistanceMatrixResponse distanceMatrix, int toVisits, Location origin, List<Location> destinations) {
        return IntStream.range(0, toVisits)
                .mapToObj(visit -> RouteSpecification.from(
                        distanceMatrix.rows().getFirst().elements().get(visit),
                        origin,
                        destinations.get(visit)))
                .toList();
    }

}
