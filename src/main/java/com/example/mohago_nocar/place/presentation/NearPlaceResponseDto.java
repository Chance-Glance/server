package com.example.mohago_nocar.place.presentation;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.Place;

import lombok.Builder;

@Builder
public record NearPlaceResponseDto(
        String id,
        String name,
        Long festivalId,
        Location location,
        String address,
        String placeUrl,
        String category
) {
    public static NearPlaceResponseDto of(Long festivalId, Place place) {
        return new NearPlaceResponseDtoBuilder()
                .id(place.getId())
                .name(place.getName())
                .festivalId(festivalId)
                .location(place.getLocation())
                .address(place.getAddress())
                .placeUrl(place.getPlaceUrl())
                .category(place.getCategory().name())
                .build();
    }
}
