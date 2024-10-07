package com.example.mohago_nocar.global.common.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

    private Double longitude; // x
    private Double latitude; // y

    public static Location from(Double longitude, Double latitude) {
        return Location.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
