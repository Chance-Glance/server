package com.example.mohago_nocar.transit.infrastructure.externalApi.google.dto.response;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import lombok.Builder;

/**
 *
 * @param distanceInKm
 * @param durationInMinutes
 * @param origin
 * @param destination
 */
@Builder
public record RouteSpecification(
        Double distanceInKm,
        Long durationInMinutes,
        Location origin,
        Location destination
) {

    public static RouteSpecification from(
            GoogleDistanceMatrixResponse.Element element,
            Location origin,
            Location destination
    ) {
        return RouteSpecification.builder()
                .distanceInKm(element.distance().value() / 1000.0)
                .durationInMinutes(element.duration().value() / 60L)
                .origin(origin)
                .destination(destination)
                .build();
    }

    public boolean isEqualLocation(Location origin, Location destination) {
        return this.origin.equals(origin) && this.destination.equals(destination);
    }

}
