package com.example.mohago_nocar.place.presentation;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.model.OperatingSchedule;
import com.example.mohago_nocar.place.domain.model.PlaceType;
import java.util.List;
import lombok.Builder;

@Builder
public record NearPlaceResponseDto(
        Long id,
        String name,
        Long festivalId,
        List<String> operatingSchedule,
        Location location,
        String address,
        String description,
        PlaceType placeType,
        String googlePlaceId,
        List<String> imageUrlList
) {
    public static NearPlaceResponseDto of(FestivalNearPlace festivalNearPlace, List<String> operatingHours, List<String> imageUrlList) {
        return new NearPlaceResponseDtoBuilder()
                .id(festivalNearPlace.getId())
                .name(festivalNearPlace.getLocation().getPlaceName())
                .festivalId(festivalNearPlace.getFestivalId())
                .operatingSchedule(operatingHours)
                .location(festivalNearPlace.getLocation())
                .address(festivalNearPlace.getAddress())
                .description(festivalNearPlace.getDescription())
                .placeType(festivalNearPlace.getPlaceType())
                .googlePlaceId(festivalNearPlace.getGooglePlaceId())
                .imageUrlList(imageUrlList)
                .build();
    }
}
