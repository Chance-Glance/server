package com.example.mohago_nocar.transit.domain.model;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class RoutePoint {

    private String name;
    private double latitude;
    private double longitude;

    public static RoutePoint from(String name, Double longitude, Double latitude) {
        return RoutePoint.builder()
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    public static RoutePoint parse(Location location) {
        return RoutePoint.from(location.getPlaceName(), location.getLongitude(), location.getLatitude());
    }
}
