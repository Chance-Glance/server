package com.example.mohago_nocar.plan.domain.model;

import com.example.mohago_nocar.festival.domain.model.Festival;
import com.example.mohago_nocar.global.common.domain.vo.Location;
import com.example.mohago_nocar.place.domain.model.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TravelLocationWithName {

    private String name;
    private Location location;

    public static TravelLocationWithName of(Festival festival) {
        return TravelLocationWithName.builder()
                .name(festival.getName())
                .location(festival.getLocation())
                .build();
    }

    public static TravelLocationWithName of(Place place) {
        return TravelLocationWithName.builder()
                .name(place.getName())
                .location(place.getLocation())
                .build();
    }

    @Builder
    private TravelLocationWithName(String name, Location location) {
        this.name = name;
        this.location = location;
    }

}
