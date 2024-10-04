package com.example.mohago_nocar.festival.presentation.response;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import lombok.Builder;

@Builder
public record FestivalLocationResponseDto(
        Location location
) {
    public static FestivalLocationResponseDto of(Location location) {
        return new FestivalLocationResponseDtoBuilder()
                .location(location)
                .build();
    }
}
