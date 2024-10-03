package com.example.mohago_nocar.global.common.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

    private Double latitude;
    private Double longitude;

    public static Location from(Double latitude, Double longitude) {
        return Location.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
