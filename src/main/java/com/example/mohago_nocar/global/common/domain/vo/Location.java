package com.example.mohago_nocar.global.common.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"placeName", "longitude", "latitude"})
public class Location {

    private String placeName;
    private Double longitude; // x
    private Double latitude; // y

    public static Location from(String name, Double longitude, Double latitude) {
        return Location.builder()
                .placeName(name)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

}
