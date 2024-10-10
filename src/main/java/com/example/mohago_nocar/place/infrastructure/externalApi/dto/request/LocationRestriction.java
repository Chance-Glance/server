package com.example.mohago_nocar.place.infrastructure.externalApi.dto.request;

import lombok.Builder;

@Builder
public record LocationRestriction(
        Circle circle
) {
    public static LocationRestriction of(double latitude, double longitude, double radius) {
        return LocationRestriction.builder()
                .circle(Circle.of(latitude, longitude, radius))
                .build();
    }

    @Builder
    public record Circle(
            Center center,
            double radius
    ) {
        public static Circle of(double latitude, double longitude, double radius) {
            return Circle.builder()
                    .center(Center.of(latitude, longitude))
                    .radius(radius)
                    .build();
        }
    }

    @Builder
    public record Center(
            double latitude,
            double longitude
    ) {
        public static Center of(double latitude, double longitude) {
            return Center.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
        }
    }
}
