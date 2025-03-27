package com.example.mohago_nocar.place.infrastructure.externalApi.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceResponseDto {

    private final String id;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final Double rating;
    private final Integer ratingCount;
    private final String placeName;
    private final String placeType;
    private final List<String> photos;
    private final String editorialSummary;
    private final String generativeSummary;
    private final List<String> schedule;

    private static final String NO_DESCRIPTION_MESSAGE = "장소 소개가 제공되지 않습니다";

    private static final String N0_SCHEDULE_MESSAGE = "영업 시간이 제공되지 않는 장소입니다";

    public static PlaceResponseDto of(
            String id,
            String address,
            Double latitude,
            Double longitude,
            Double rating,
            Integer userRatingCount,
            String placeName,
            String placeType,
            List<String> photos,
            String editorialSummary,
            String generativeSummary,
            List<String> schedule
    ) {
        return PlaceResponseDto.builder()
                .id(id)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .rating(rating)
                .ratingCount(userRatingCount)
                .placeName(placeName)
                .placeType(placeType)
                .photos(photos)
                .editorialSummary(editorialSummary)
                .generativeSummary(generativeSummary)
                .schedule(schedule)
                .build();
    }

    public PlaceResponseDto withUpdatedPhotos(List<String> photos) {
        return PlaceResponseDto.builder()
                .id(this.id)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .rating(this.rating)
                .ratingCount(this.ratingCount)
                .placeName(this.placeName)
                .placeType(this.placeType)
                .photos(photos)
                .editorialSummary(this.editorialSummary)
                .generativeSummary(this.generativeSummary)
                .schedule(this.schedule)
                .build();
    }

    public String getDescription() {
        if (editorialSummary != null && !editorialSummary.isEmpty()) {
            return editorialSummary;
        }

        if (generativeSummary != null && !generativeSummary.isEmpty()) {
            return generativeSummary;
        }

        return NO_DESCRIPTION_MESSAGE;
    }

    public List<String> getSchedule() {
        if (this.schedule.isEmpty()) {
            return List.of(N0_SCHEDULE_MESSAGE);
        }

        return this.schedule;
    }
}
