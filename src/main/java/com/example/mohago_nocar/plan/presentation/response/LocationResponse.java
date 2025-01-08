package com.example.mohago_nocar.plan.presentation.response;

import lombok.Builder;

@Builder
public record LocationResponse(
        String name,
        double longitude,
        double latitude
) {

    public static LocationResponse of(
            String name,
            double longitude,
            double latitude
    ){
        return LocationResponse.builder()
                .name(name)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

}
