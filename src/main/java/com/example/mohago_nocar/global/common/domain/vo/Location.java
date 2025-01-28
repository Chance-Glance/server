package com.example.mohago_nocar.global.common.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"longitude", "latitude"})
@ToString
public class Location {

    private Double longitude; // x
    private Double latitude; // y

    public static Location from(Double longitude, Double latitude) {
        return Location.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    public static Location from(String longitude, String latitude) {
        return Location.from(Double.valueOf(longitude), Double.valueOf(latitude));
    }

}
