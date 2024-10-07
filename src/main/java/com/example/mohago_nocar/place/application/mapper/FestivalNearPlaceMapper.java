package com.example.mohago_nocar.place.application.mapper;

import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlace;
import com.example.mohago_nocar.place.domain.model.FestivalNearPlaceImage;
import com.example.mohago_nocar.place.domain.model.OperatingSchedule;
import com.example.mohago_nocar.place.domain.model.PlaceType;
import com.example.mohago_nocar.place.infrastructure.externalApi.dto.response.PlaceResponseDto;
import java.util.List;


public class FestivalNearPlaceMapper {

    public static FestivalNearPlace convertToFestivalNearPlace(Long festivalId, PlaceResponseDto dto) {

        String placeName = dto.getPlaceName();
        OperatingSchedule schedule = getSchedule(dto);
        Location location = getLocation(dto);
        String address = dto.getAddress();
        String description = dto.getDescription();
        PlaceType placeType = getPlaceType(dto);
        String googlePlaceId = dto.getId();

        return FestivalNearPlace.from(festivalId, placeName, schedule, location, address, description, placeType, googlePlaceId);
    }

    private static OperatingSchedule getSchedule(PlaceResponseDto dto) {
        return OperatingSchedule.from(dto.getSchedule());
    }

    private static Location getLocation(PlaceResponseDto dto) {
        return Location.from(dto.getLongitude(), dto.getLatitude());
    }

    private static PlaceType getPlaceType(PlaceResponseDto dto) {
        return PlaceType.from(dto.getPlaceType());
    }

    public static List<FestivalNearPlaceImage> convertToFestivalNearPlaceImage(Long festivalId, List<String> photos) {
        return photos.stream().map(photo -> FestivalNearPlaceImage.from(festivalId, photo)).toList();
    }
}
